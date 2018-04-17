package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotServiceClientFallback implements BotServiceClient {

    private static Logger log = LoggerFactory.getLogger(BotServiceClientFallback.class);

    private final ObjectMapper mapper;

    @Autowired
    public BotServiceClientFallback(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PushTokenDTO getPushTokenByHid(String hid, String token) {
        log.error("request to bot-service failed");
        return new PushTokenDTO(null);
    }

    @Override
    public JsonNode messages(String hid, String token) {
        log.error("request to bot-service failed");
        return mapper.createArrayNode();
    }

    @Override
    public JsonNode messages(String hid, int count, String token) {
        log.error("request to bot-service failed");
        return mapper.createArrayNode();
    }

    @Override
    public List<BackOfficeMessage> fetch(long time, String token) {
        log.error("request to bot-service failed");
        return new ArrayList<>();
    }

    @Override
    public void response(BackOfficeResponseDTO message, String token) {
        log.error("request to bot-service failed");
    }

    @Override
    public void answer(BackOfficeResponseDTO answer, String token) {
        log.error("request to bot-service failed");
    }

}
