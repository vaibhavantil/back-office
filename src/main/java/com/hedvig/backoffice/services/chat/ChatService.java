package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.ChatMessage;

import java.util.List;

public interface ChatService {

    void retranslate(String hid, ChatMessage message);
    void retranslate(String hid, List<ChatMessage> messages);
    void append(String hid, ChatMessage message);
    void messages(String hid);
    void messages(String hid, int count);
    void close(String sessionId);
    void subscribe(String hid, String subId, String sessionId);
    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
