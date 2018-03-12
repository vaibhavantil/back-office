package com.hedvig.backoffice.services.chat;

import com.google.common.collect.Sets;
import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatUpdatesServiceImpl implements ChatUpdatesService {

    private static Logger logger = LoggerFactory.getLogger(ChatUpdatesServiceImpl.class);

    private final ChatService chatService;
    private final BotService botService;
    private final SystemSettingsService systemSettingsService;
    private final QuestionService questionService;

    private final Set<String> questionId;

    @Autowired
    public ChatUpdatesServiceImpl(ChatService chatService,
                                  BotService botService,
                                  SystemSettingsService systemSettingsService,
                                  QuestionService questionService,
                                  @Value("${botservice.questionId}") String[] questionId) {

        this.chatService = chatService;
        this.botService = botService;
        this.systemSettingsService = systemSettingsService;
        this.questionService = questionService;
        this.questionId = Sets.newHashSet(questionId);

        logger.info("CHAT UPDATE SERVICE: ");
        logger.info("question ids: " + Arrays.toString(questionId));
    }

    @Override
    public void update() throws ChatUpdateException {
        List<BackOfficeMessage> fetched;

        try {
            fetched = botService.fetch(lastTimestamp());
        } catch (BotServiceException e) {
            throw new ChatUpdateException(e);
        }

        if (fetched.size() == 0) {
            return;
        }

        logger.info("bot-service: fetched " + fetched.size() + " messages");

        List<BotMessage> messages = fetched.stream().map(f -> {
            try {
                return f.toBotMessage();
            } catch (BotMessageException e) {
                logger.error("Error during parsing message from bot-service", e);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        HashMap<String, Long> idStatistics = new HashMap<>();
        for (BotMessage m : messages) {
            Long count = idStatistics.getOrDefault(m.getId(), 0L) + 1;
            idStatistics.put(m.getId(), count);
        }

        logger.info(idStatistics.toString());

        Map<String, List<BotMessage>> updates = messages.stream()
                .collect(Collectors.groupingBy(BotMessage::getHid));

        Instant lastTimestamp = messages.stream()
                .max(Comparator.comparing(BotMessage::getTimestamp))
                .map(BotMessage::getTimestamp)
                .orElse(new Date().toInstant());

        systemSettingsService.update(SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP, lastTimestamp.plusMillis(1).toString());

        updates.forEach((k, v) -> chatService.send(k, Message.chat(v)));

        List<BotMessage> questions = messages.stream()
                .filter(m -> questionId.contains(m.getId()) && !m.isBotMessage())
                .collect(Collectors.toList());

        logger.info("fetched questions: " + questions.size());
        questionService.addNewQuestions(questions);
    }

    private Instant lastTimestamp() {
        SystemSetting setting = systemSettingsService.getSetting(
                SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP,
                new Date().toInstant().toString());

        return Instant.parse(setting.getValue());
    }
}
