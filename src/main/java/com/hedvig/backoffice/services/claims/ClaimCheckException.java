package com.hedvig.backoffice.services.claims;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClaimCheckException extends ClaimException {

    public ClaimCheckException(String msg) {
        super(msg);
    }

}
