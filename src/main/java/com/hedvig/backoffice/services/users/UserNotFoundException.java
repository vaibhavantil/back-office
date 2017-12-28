package com.hedvig.backoffice.services.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String user) {
        super(String.format("user by query string '%s' not found", user));
    }

}
