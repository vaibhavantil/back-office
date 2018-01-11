package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.services.chat.data.ChatMessage;

import java.time.Instant;
import java.util.List;

public interface BotService {

    List<ChatMessage> messages(String hid) throws BotServiceException;
    List<ChatMessage> messages(String hid, int count) throws BotServiceException;
    List<ChatMessage> updates(ChatContext chat) throws BotServiceException;
    void response(String hid, ChatMessage message) throws BotServiceException;

}
