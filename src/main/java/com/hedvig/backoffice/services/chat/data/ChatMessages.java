package com.hedvig.backoffice.services.chat.data;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Value;

@Value
public class ChatMessages implements MessagePayload {

  private List<JsonNode> messages;
}
