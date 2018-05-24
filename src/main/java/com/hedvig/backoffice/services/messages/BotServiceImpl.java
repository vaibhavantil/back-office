package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BotServiceImpl implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceImpl.class);

    private BotServiceClient botServiceClient;

    @Autowired
    private BotServiceImpl(BotServiceClient botServiceClient) {
        this.botServiceClient = botServiceClient;

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceImpl.class.getName());
    }

    @Override
    public List<BotMessage> messages(String memberId, String token) {
        JsonNode root = botServiceClient.messages(memberId, token);
        return parseMessages(root);
    }

    @Override
    public List<BotMessage> messages(String memberId, int count, String token) {
        JsonNode root = botServiceClient.messages(memberId, count, token);
        return parseMessages(root);
    }

    @Override
    public List<BackOfficeMessage> fetch(Instant timestamp, String token) {
        return botServiceClient.fetch(timestamp.toEpochMilli(), token);
    }

    @Override
    public void response(String memberId, String message, String token) {
        botServiceClient.response(new BackOfficeResponseDTO(memberId, message), token);
    }

    @Override
    public void answerQuestion(String memberId, String answer, String token) {
        botServiceClient.answer(new BackOfficeResponseDTO(memberId, answer), token);
    }

    private List<BotMessage> parseMessages(JsonNode root) {
        if (root == null) {
            throw new ExternalServiceException("bot-service internal error or service unavailable");
        }

        Iterable<Map.Entry<String, JsonNode>> iterable = root::fields;

        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(e -> {
                        try {
                            return new BotMessage(e.getValue().toString());
                        } catch (BotMessageException ex) {
                            logger.error(ex.getMessage(), ex);
                            return null;
                        }
                    })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

	@Override
	public PushTokenDTO pushTokenId(String memberId, String token) {
        return botServiceClient.getPushTokenByMemberId(memberId, token);
	}
}
