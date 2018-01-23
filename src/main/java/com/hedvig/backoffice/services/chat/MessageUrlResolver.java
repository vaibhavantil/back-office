package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.messages.BotServiceMessage;

public interface MessageUrlResolver {

    void resolveUrls(BotServiceMessage message);

}
