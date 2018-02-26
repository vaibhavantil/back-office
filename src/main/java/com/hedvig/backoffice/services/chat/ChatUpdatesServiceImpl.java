package com.hedvig.backoffice.services.chat;

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
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatUpdatesServiceImpl implements ChatUpdatesService {

    private static Logger logger = LoggerFactory.getLogger(ChatUpdatesServiceImpl.class);

    private final ChatService chatService;
    private final BotService botService;
    private final UpdatesService updatesService;
    private final SystemSettingsService systemSettingsService;
    private final QuestionService questionService;

    @Autowired
    public ChatUpdatesServiceImpl(ChatService chatService,
                                  BotService botService,
                                  UpdatesService updatesService,
                                  SystemSettingsService systemSettingsService,
                                  QuestionService questionService) {

        this.chatService = chatService;
        this.botService = botService;
        this.updatesService = updatesService;
        this.systemSettingsService = systemSettingsService;
        this.questionService = questionService;
    }

    @Override
    public void update() throws ChatUpdateException {
        List<BackOfficeMessage> messages;
        SystemSetting setting = systemSettingsService.getSetting(
                SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP,
                new Date().toInstant().toString());

        try {
            Instant timestamp = Instant.parse(setting.getValue());
            messages = botService.fetch(timestamp);
        } catch (BotServiceException e) {
            throw new ChatUpdateException(e);
        }

        if (messages.size() == 0) {
            return;
        }

        logger.info("bot-service: fetched " + messages.size() + " messages");
        updatesService.append(messages.size(), UpdateType.CHATS);

        Map<String, List<BotMessage>> updates = messages.stream()
                .collect(Collectors.groupingBy(BackOfficeMessage::getUserId, Collectors.mapping(m -> {
                    try {
                        return new BotMessage(m.getMsg(), false);
                    } catch (BotMessageException e) {
                        logger.error("Error during parsing message from bot-service", e);
                    }
                    return null;
                }, Collectors.toList())));

        Instant lastTimestamp = updates.values().stream()
                .flatMap(Collection::stream)
                .max(Comparator.comparing(BotMessage::getTimestamp))
                .map(BotMessage::getTimestamp)
                .orElse(new Date().toInstant());

        setting.setValue(lastTimestamp.plusMillis(1).toString());
        systemSettingsService.saveSetting(setting);

        updates.forEach((k, v) -> chatService.send(k, Message.chat(v)));

        questionService.save(messages.stream()
                .map(m -> new QuestionDTO(m.getUserId(), m.getMsg()))
                .collect(Collectors.toList()));
    }
}
