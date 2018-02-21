package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import java.time.Instant;
import java.util.List;

public interface BotService {

    List<BotServiceMessage> messages(String hid) throws BotServiceException;
    List<BotServiceMessage> messages(String hid, int count) throws BotServiceException;
    List<BackOfficeMessage> fetch(Instant timestamp) throws BotServiceException;
    void response(String hid, BotServiceMessage message) throws BotServiceException;
    String pushTokenId(String hid);
}
