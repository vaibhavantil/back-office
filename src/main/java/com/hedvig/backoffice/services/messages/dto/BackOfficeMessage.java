package com.hedvig.backoffice.services.messages.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.BotMessageException;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

@AllArgsConstructor
@Value
public class BackOfficeMessage {

  private String userId;
  private JsonNode msg;

  public BotMessage toBotMessage() throws BotMessageException {
    val message = new BotMessage(msg);
    message.setMemberId(userId);
    return message;
  }
}
