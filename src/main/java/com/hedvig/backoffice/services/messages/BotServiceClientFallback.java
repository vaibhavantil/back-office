package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BotServiceClientFallback implements BotServiceClient {

    @Override
    public PushTokenDTO getPushTokenByHid(String hid, String token) {
        log.error("request to bot-service failed");
        return new PushTokenDTO(null);
    }

    @Override
    public JsonNode messages(String hid, String token) {
        log.error("request to bot-service failed");
        return null;
    }

    @Override
    public JsonNode messages(String hid, int count, String token) {
        log.error("request to bot-service failed");
        return null;
    }

    @Override
    public List<BackOfficeMessage> fetch(long time, String token) {
        log.error("request to bot-service failed");
        return null;
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
