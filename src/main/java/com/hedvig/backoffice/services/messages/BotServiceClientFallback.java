package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BotServiceClientFallback implements BotServiceClient {

    private static Logger log = LoggerFactory.getLogger(BotServiceClientFallback.class);

    @Override
    public PushTokenDTO getPushTokenByHid(String hid) {
        log.error("bot-service unavailable");
        return new PushTokenDTO("");
    }

    @Override
    public void response(BackOfficeMessage message) {
        log.error("bot-service unavailable");
    }

    @Override
    public void answer(BackOfficeMessage message) {
        log.error("bot-service unavailable");
    }

}
