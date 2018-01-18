package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.Message;

import java.util.List;

public interface ChatService {

    void send(String hid, Message message);
    void append(String hid, String message);
    void messages(String hid);
    void messages(String hid, int count);
    void close(String sessionId);
    void subscribe(String hid, String subId, String sessionId);
    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
