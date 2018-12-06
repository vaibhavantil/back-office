package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class BackOfficeMessage {

  private String userId;
  private BotMessageDTO msg;
}
