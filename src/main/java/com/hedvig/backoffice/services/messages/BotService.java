package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;

import java.util.List;

public interface BotService {

    List<Message> messages(String hid);
    List<Message> messages(String hid, int count);
    List<Message> updates(String hid);
    void response(String hid, Message message);

}
