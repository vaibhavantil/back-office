package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.Message;


public interface ChatService {

    void send(String hid, String personnelId, Message message);

    void append(String hid, String message, String personnelId, String token);

    void messages(String hid, String personnelId, String token);

    void messages(String hid, int count, String personnelId, String token);

    void close(String sessionId);

    void subscribe(String hid, String subId, String sessionId, String principalId);

    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
