package com.hedvig.backoffice.services.messages;

import java.time.Instant;
import java.util.List;

public interface BotService {

    List<BotServiceMessage> messages(String hid) throws BotServiceException;
    List<BotServiceMessage> messages(String hid, int count) throws BotServiceException;
    List<BotServiceMessage> updates(String hid, Instant timestamp) throws BotServiceException;
    void response(String hid, BotServiceMessage message) throws BotServiceException;

}
