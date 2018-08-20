package com.hedvig.backoffice.services.personnel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class TokenRefreshException extends RuntimeException {

  private static final long serialVersionUID = 114750209111364082L;

  public TokenRefreshException(Throwable t) {
    super(t);
  }

  public TokenRefreshException() {
    super();
  }
}
