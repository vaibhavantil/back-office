package com.hedvig.backoffice.services.chat.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

import java.util.List;

@Value
public class ChatMessages implements MessagePayload {

    private List<JsonNode> messages;

}
