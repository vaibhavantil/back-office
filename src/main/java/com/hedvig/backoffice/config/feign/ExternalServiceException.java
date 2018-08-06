package com.hedvig.backoffice.config.feign;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ExternalServiceException extends RuntimeException {

  public ExternalServiceException(String cause) {
    super(cause);
  }
}
