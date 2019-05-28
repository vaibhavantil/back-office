package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BotService {
  List<BotMessageDTO> messages(String memberId, String token);

  List<BotMessageDTO> messages(String memberId, int count, String token);

  List<BackOfficeMessage> fetch(Instant timestamp, String token);

  ExpoPushTokenDTO getExpoPushToken(String memberId, String token);

  void response(String memberId, String message, boolean forceSendMessage, String token);

  void answerQuestion(String memberId, String answer, String token);

  Optional<FirebasePushTokenDTO> getFirebasePushToken(String memberId, String token);

  List<FileUploadDTO> files(String memberId, String token);
}
