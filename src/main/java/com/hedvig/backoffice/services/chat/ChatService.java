package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.ChatMessage;

public interface ChatService {

    void retranslate(String hid, ChatMessage message);
    void append(String hid, ChatMessage message);
    void close(String sessionId);
    void subscribe(String hid, String subId, String sessionId);
    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
