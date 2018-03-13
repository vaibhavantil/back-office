package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;

import java.time.Instant;
import java.util.List;

public interface BotService {
    List<BotMessage> messages(String hid);
    List<BotMessage> messages(String hid, int count);
    List<BackOfficeMessage> fetch(Instant timestamp);
    String pushTokenId(String hid);
    void response(String hid, BotMessage message);
    void answerQuestion(String hid, String answer);

}
