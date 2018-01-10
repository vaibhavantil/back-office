package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.messages.data.Message;
import com.hedvig.backoffice.services.users.UserNotFoundException;

public interface ChatService {

    void retranslate(String hid, Message message);
    void append(String hid, Message message) throws UserNotFoundException;
    void close(String sessionId);
    void subscribe(String hid, String subId, String sessionId);
    void unsubscribe(String subId, String sessionId);

    String getTopicPrefix();

}
