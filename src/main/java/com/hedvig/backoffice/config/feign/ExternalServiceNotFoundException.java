package com.hedvig.backoffice.config.feign;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExternalServiceNotFoundException extends ExternalServiceBadRequestException {
  public ExternalServiceNotFoundException(String message, String cause) {
    super(message, cause);
  }
}
