package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.dto.BackOfficeAnswerDTO;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BotServiceImpl implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceImpl.class);

    private BotServiceClient botServiceClient;
    private String baseUrl;

    @Autowired
    private BotServiceImpl(@Value("${botservice.baseUrl}") String baseUrl, BotServiceClient botServiceClient) {
        this.baseUrl = baseUrl;
        this.botServiceClient = botServiceClient;

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceImpl.class.getName());
    }

    @Override
    public List<BotMessage> messages(String hid) {
        JsonNode root = botServiceClient.messages(hid);
        return parseMessages(root);
    }

    @Override
    public List<BotMessage> messages(String hid, int count) {
        JsonNode root = botServiceClient.messages(hid, count);
        return parseMessages(root);
    }

    @Override
    public List<BackOfficeMessage> fetch(Instant timestamp) {
        RestTemplate template = new RestTemplate();
        try {
            String time = Long.toString(timestamp.toEpochMilli());
            ResponseEntity<BackOfficeMessage[]> messages
                    = template.getForEntity(baseUrl + "/_/messages/" + time, BackOfficeMessage[].class);

            return Arrays.asList(messages.getBody());
        } catch (RestClientException e) {
            logger.error("request to bot-service failed", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void response(String hid, BotMessage message) {
        botServiceClient.response(new BackOfficeMessage(hid, message.getMessage()));
    }

    @Override
    public void answerQuestion(String hid, String answer) {
        botServiceClient.answer(new BackOfficeAnswerDTO(hid, answer));
    }

    private List<BotMessage> parseMessages(JsonNode root) {
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
	public String pushTokenId(String hid) {
        val pushTokenDto = botServiceClient.getPushTokenByHid(hid);
        return pushTokenDto.getToken();
	}
}
