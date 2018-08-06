package com.hedvig.backoffice.services.chat;

import com.google.common.collect.Sets;
import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatUpdatesServiceImpl implements ChatUpdatesService {

  private final ChatService chatService;
  private final BotService botService;
  private final SystemSettingsService systemSettingsService;
  private final QuestionService questionService;
  private final ChatContextRepository chatContextRepository;

  private final Set<String> questionId;

  private ExecutorService executor;
  private AtomicBoolean serviceUnavailable;

  @Autowired
  public ChatUpdatesServiceImpl(
      ChatService chatService,
      BotService botService,
      SystemSettingsService systemSettingsService,
      QuestionService questionService,
      ChatContextRepository chatContextRepository,
      @Value("${botservice.questionId}") String[] questionId) {

    this.chatService = chatService;
    this.botService = botService;
    this.systemSettingsService = systemSettingsService;
    this.questionService = questionService;
    this.chatContextRepository = chatContextRepository;
    this.questionId = Sets.newHashSet(questionId);
    this.serviceUnavailable = new AtomicBoolean(false);

    log.info("CHAT UPDATE SERVICE: ");
    log.info("question ids: " + Arrays.toString(questionId));
  }

  @PostConstruct
  public void setup() {
    executor = Executors.newCachedThreadPool();
  }

  @PreDestroy
  public void destroy() throws InterruptedException {
    executor.shutdown();
    boolean shutdownResult = executor.awaitTermination(10, TimeUnit.SECONDS);
    if (!shutdownResult) {
      log.error("Thread pool is not shutting down");
    }
  }

  @Scheduled(fixedDelayString = "${intervals.chat}")
  @Override
  public void update() {
    List<BackOfficeMessage> fetched =
        botService.fetch(lastTimestamp(), systemSettingsService.getInternalAccessToken());

    if (fetched == null) {
      if (!serviceUnavailable.get()) {
        serviceUnavailable.set(true);
        sendErrorToAll();
      }

      return;
    }

    serviceUnavailable.set(false);

    if (fetched.size() == 0) {
      return;
    }

    Instant lastTimestamp = null;
    Map<String, List<BotMessage>> messages = new HashMap<>();
    List<BotMessage> questions = new ArrayList<>();

    for (BackOfficeMessage backOfficeMessage : fetched) {
      BotMessage message;

      try {
        message = backOfficeMessage.toBotMessage();
      } catch (BotMessageException e) {
        log.error("Error during parsing message from bot-service", e);
        continue;
      }

      List<BotMessage> messagesForMemberId =
          messages.computeIfAbsent(backOfficeMessage.getUserId(), key -> new ArrayList<>());
      messagesForMemberId.add(message);

      if (questionId.contains(message.getId()) && !message.isBotMessage()) {
        questions.add(message);
      }

      if (lastTimestamp == null || lastTimestamp.isBefore(message.getTimestamp())) {
        lastTimestamp = message.getTimestamp();
      }
    }

    if (lastTimestamp != null) {
      log.info("Messages found - updating timestamp to: {} + 1", lastTimestamp.toString());
      systemSettingsService.update(
          SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP, lastTimestamp.plusMillis(1).toString());
    }

    if (log.isDebugEnabled()) {
      log.info(
          "bot-service: fetched "
              + fetched.size()
              + " messages and "
              + questions.size()
              + " questions");
    }

    if (questions.size() > 0) {
      CompletableFuture.runAsync(() -> addQuestions(questions), executor)
          .exceptionally(
              e -> {
                log.error("error while adding questions", e);
                return null;
              });
    }

    messages.forEach(
        (k, v) ->
            CompletableFuture.runAsync(() -> sendMessages(k, v))
                .exceptionally(
                    e -> {
                      log.error("error while message sending", e);
                      return null;
                    }));
  }

  private Instant lastTimestamp() {
    SystemSetting setting =
        systemSettingsService.getSetting(
            SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP, new Date().toInstant().toString());

    return Instant.parse(setting.getValue());
  }

  private void addQuestions(List<BotMessage> questions) {
    questionService.addNewQuestions(questions);
  }

  private void sendMessages(String memberId, List<BotMessage> messages) {
    List<Personnel> personnels =
        chatContextRepository.findPersonnelsWithActiveChatsByMemberId(memberId);
    Message m = Message.chat(messages);
    personnels.forEach(p -> chatService.send(memberId, p.getId(), m));
  }

  private void sendErrorToAll() {
    List<ChatContext> chats = chatContextRepository.findActiveChats();
    Message m = Message.error(500, "bot-service unavailable");
    chats.forEach(c -> chatService.send(c.getMemberId(), c.getPersonnel().getId(), m));
  }
}
