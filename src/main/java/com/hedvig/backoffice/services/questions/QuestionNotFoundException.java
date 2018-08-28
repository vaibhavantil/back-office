package com.hedvig.backoffice.services.questions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNotFoundException extends Exception {

  private static final long serialVersionUID = 3751062057237540439L;

  public QuestionNotFoundException(String memberId) {
    super("question for user with memberId = " + memberId + " not found");
  }
}
