package com.hedvig.backoffice.services.personnel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class TokenRefreshException extends RuntimeException {

    public TokenRefreshException(Throwable t) {
        super(t);
    }

    public TokenRefreshException() {
        super();
    }
}
