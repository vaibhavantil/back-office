package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.messages.dto.BotMessage;

public interface MessageUrlResolver {

    void resolveUrls(BotMessage message);

}
