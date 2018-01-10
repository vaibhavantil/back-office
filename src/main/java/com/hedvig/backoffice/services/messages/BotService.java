package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;

import java.util.List;

public interface BotService {

    List<Message> messages(String hid) throws BotServiceException;
    List<Message> messages(String hid, int count) throws BotServiceException;
    List<Message> updates(String hid) throws BotServiceException;
    void response(String hid, Message message) throws BotServiceException;

}
