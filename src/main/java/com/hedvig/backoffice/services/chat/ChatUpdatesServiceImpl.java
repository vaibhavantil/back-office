package com.hedvig.backoffice.services.chat;

import com.google.common.collect.Sets;
import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ChatUpdatesServiceImpl implements ChatUpdatesService {

  private final BotService botService;
  private final SystemSettingsService systemSettingsService;
  private final QuestionService questionService;

  private final Set<String> questionId;

  private ExecutorService executor;
  private AtomicBoolean serviceUnavailable;

  @Autowired
  public ChatUpdatesServiceImpl(
    BotService botService,
    SystemSettingsService systemSettingsService,
    QuestionService questionService,
    @Value("${botservice.questionId}") String[] questionId
  ) {
    this.botService = botService;
    this.systemSettingsService = systemSettingsService;
    this.questionService = questionService;
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
    List<BackOfficeMessage> fetched = botService.fetch(lastTimestamp(), systemSettingsService.getInternalAccessToken());

    if (fetched == null) {
      if (!serviceUnavailable.get()) {
        serviceUnavailable.set(true);
      }

      return;
    }

    serviceUnavailable.set(false);

    if (fetched.size() == 0) {
      return;
    }

    Instant lastTimestamp = null;
    Map<String, List<BotMessageDTO>> messages = new HashMap<>();
    List<BotMessageDTO> questions = new ArrayList<>();

    for (BackOfficeMessage backOfficeMessage : fetched) {
      BotMessageDTO message = backOfficeMessage.getMsg();

      List<BotMessageDTO> messagesForMemberId =
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
  }

  private Instant lastTimestamp() {
    SystemSetting setting =
      systemSettingsService.getSetting(
        SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP, new Date().toInstant().toString());

    return Instant.parse(setting.getValue());
  }

  private void addQuestions(List<BotMessageDTO> questions) {
    questionService.addNewQuestions(questions);
  }
}
