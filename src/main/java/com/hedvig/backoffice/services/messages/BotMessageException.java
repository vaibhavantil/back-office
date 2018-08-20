package com.hedvig.backoffice.services.messages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BotMessageException extends Exception {

  private static final long serialVersionUID = 2886759084672023181L;

  public BotMessageException(String msg) {
    super(msg);
  }

  public BotMessageException(Throwable t) {
    super(t);
  }
}
