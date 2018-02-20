package com.hedvig.backoffice.services.claims;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClaimBadRequestException extends ClaimException {

    public ClaimBadRequestException(Throwable t) {
        super(t);
    }

    public ClaimBadRequestException(String msg) {
        super(msg);
    }

}
