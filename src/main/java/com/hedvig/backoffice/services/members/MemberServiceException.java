package com.hedvig.backoffice.services.members;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class MemberServiceException extends RuntimeException {

    public MemberServiceException() {
        super("member registry service unavailable");
    }

}
