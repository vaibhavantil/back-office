package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.messages.dto.BackOfficeAnswerDTO;
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
    public PushTokenDTO getPushTokenByHid(String hid) {
        log.error("bot-service unavailable");
        return new PushTokenDTO("");
    }

    @Override
    public JsonNode messages(String hid) {
        log.error("bot-service unavailable");
        return mapper.createArrayNode();
    }

    @Override
    public JsonNode messages(String hid, int count) {
        log.error("bot-service unavailable");
        return mapper.createArrayNode();
    }

    @Override
    public List<BackOfficeMessage> fetch(long time) {
        log.error("bot-service unavailable");
        return new ArrayList<>();
    }

    @Override
    public void response(BackOfficeMessage message) {
        log.error("bot-service unavailable");
    }

    @Override
    public void answer(BackOfficeAnswerDTO answer) {
        log.error("bot-service unavailable");
    }

}
