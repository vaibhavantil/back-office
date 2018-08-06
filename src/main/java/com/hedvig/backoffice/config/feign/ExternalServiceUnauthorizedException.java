package com.hedvig.backoffice.config.feign;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExternalServiceUnauthorizedException extends ExternalServiceBadRequestException {

  public ExternalServiceUnauthorizedException(String message, String cause) {
    super(message, cause);
  }
}
