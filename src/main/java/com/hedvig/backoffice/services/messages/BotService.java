package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;

import java.time.Instant;
import java.util.List;

public interface BotService {
    List<BotMessage> messages(String memberId, String token);
    List<BotMessage> messages(String memberId, int count, String token);
    List<BackOfficeMessage> fetch(Instant timestamp, String token);
    PushTokenDTO pushTokenId(String memberId, String token);
    void response(String memberId, String message, String token);
    void answerQuestion(String memberId, String answer, String token);
}
