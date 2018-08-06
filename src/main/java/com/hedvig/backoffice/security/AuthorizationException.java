package com.hedvig.backoffice.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends Exception {

  public AuthorizationException(String message) {
    super(message);
  }

  public AuthorizationException() {
    super();
  }
}
