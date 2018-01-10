package com.hedvig.backoffice.services.messages.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PayloadMessage implements Message {

    private String payload;

    @Override
    public String getPayload() {
        return payload;
    }
}
