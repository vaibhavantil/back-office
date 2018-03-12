package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;

import java.time.Instant;
import java.util.List;

public interface BotService {
    List<BotMessage> messages(String hid) throws BotServiceException;
    List<BotMessage> messages(String hid, int count) throws BotServiceException;
    List<BackOfficeMessage> fetch(Instant timestamp) throws BotServiceException;
    String pushTokenId(String hid);
    void response(String hid, BotMessage message);
    void answerQuestion(String hid, BotMessage message);

}
