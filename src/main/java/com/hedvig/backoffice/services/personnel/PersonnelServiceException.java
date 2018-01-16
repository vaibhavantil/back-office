package com.hedvig.backoffice.services.personnel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonnelServiceException extends Exception {

    public PersonnelServiceException(String msg) {
        super(msg);
    }

}
