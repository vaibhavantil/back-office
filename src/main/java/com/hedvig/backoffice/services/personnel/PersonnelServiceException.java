package com.hedvig.backoffice.services.personnel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonnelServiceException extends Exception {

  private static final long serialVersionUID = 3393332384882223989L;

  public PersonnelServiceException(String msg) {
    super(msg);
  }
}
