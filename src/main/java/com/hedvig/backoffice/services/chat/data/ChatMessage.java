package com.hedvig.backoffice.services.chat.data;

import java.time.Instant;

public interface ChatMessage {

    String getPayload();
    Instant getTimestamp();

}
