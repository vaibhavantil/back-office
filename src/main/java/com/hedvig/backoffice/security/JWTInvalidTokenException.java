package com.hedvig.backoffice.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JWTInvalidTokenException extends Exception {

    public JWTInvalidTokenException(String message) {
        super(message);
    }

}
