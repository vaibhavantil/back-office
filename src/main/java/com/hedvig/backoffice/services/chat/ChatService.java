package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.Message;

public interface ChatService {

  void send(String memberId, String personnelEmail, Message message);

  void append(String memberId, String message, boolean forceSendMessage, String personnelEmail, String token);

  void messages(String memberId, String personnelEmail, String token);

  void messages(String memberId, int count, String personnelEmail, String token);

  void close(String sessionId);

  void subscribe(String memberId, String subId, String sessionId, String principalEmail);

  void unsubscribe(String subId, String sessionId);

  String getTopicPrefix();
}
