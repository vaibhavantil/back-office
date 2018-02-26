package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.dto.BotMessage;


public interface ChatService {

    void send(String hid, Message message);
    boolean append(String hid, String message);
    boolean append(String hid, BotMessage message);
    void messages(String hid);
    void messages(String hid, int count);
    void close(String sessionId);
    void subscribe(String hid, String subId, String sessionId, String principal);
    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
