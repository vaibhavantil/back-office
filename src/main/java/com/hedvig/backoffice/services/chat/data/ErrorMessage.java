package com.hedvig.backoffice.services.chat.data;

import lombok.Value;

@Value
public class ErrorMessage implements MessagePayload {

  private int code;
  private String message;
}
