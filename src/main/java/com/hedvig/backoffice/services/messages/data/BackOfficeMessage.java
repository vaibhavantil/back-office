package com.hedvig.backoffice.services.messages.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackOfficeMessage {

    public String userId;
    public JsonNode msg;

}
