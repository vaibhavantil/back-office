package com.hedvig.backoffice.services.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class UserServiceException extends Exception {

    public UserServiceException(Throwable exception) {
        super(exception);
    }

}
