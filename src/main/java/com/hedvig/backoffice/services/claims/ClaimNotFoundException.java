package com.hedvig.backoffice.services.claims;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClaimNotFoundException extends Exception {

    public ClaimNotFoundException(String msg) {
        super(msg);
    }

}
