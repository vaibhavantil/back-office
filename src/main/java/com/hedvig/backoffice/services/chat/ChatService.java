package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.Message;

public interface ChatService {

  void send(String memberId, String personnelId, Message message);

  void append(String memberId, String message, String personnelId, String token);

  void messages(String memberId, String personnelId, String token);

  void messages(String memberId, int count, String personnelId, String token);

  void close(String sessionId);

  void subscribe(String memberId, String subId, String sessionId, String principalId);

  void unsubscribe(String subId, String sessionId);

  String getTopicPrefix();
}
