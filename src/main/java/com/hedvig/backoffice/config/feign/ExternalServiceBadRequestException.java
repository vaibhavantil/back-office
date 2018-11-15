package com.hedvig.backoffice.config.feign;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExternalServiceBadRequestException extends HystrixBadRequestException {

  public ExternalServiceBadRequestException(String message, String cause) {
    super(message + " cause: " + cause);
  }
}
