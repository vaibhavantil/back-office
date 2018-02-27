package com.hedvig.backoffice.services.questions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNotFoundException extends Exception {

    public QuestionNotFoundException(String message) {
        super(message);
    }

}
