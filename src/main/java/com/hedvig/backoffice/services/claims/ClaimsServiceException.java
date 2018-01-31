package com.hedvig.backoffice.services.claims;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ClaimsServiceException extends ClaimException {

    public ClaimsServiceException(Throwable t) {
        super(t);
    }

}
