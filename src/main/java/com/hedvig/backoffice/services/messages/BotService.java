package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;

import java.time.Instant;
import java.util.List;

public interface BotService {
    List<BotMessage> messages(String hid, String token);
    List<BotMessage> messages(String hid, int count, String token);
    List<BackOfficeMessage> fetch(Instant timestamp, String token);
    PushTokenDTO pushTokenId(String hid, String token);
    void response(String hid, String message, String token);
    void answerQuestion(String hid, String answer, String token);
}
