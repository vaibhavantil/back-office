package com.hedvig.backoffice.services.members;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends Exception {

    public MemberNotFoundException(String user) {
        super(String.format("user by query string '%s' not found", user));
    }

}
