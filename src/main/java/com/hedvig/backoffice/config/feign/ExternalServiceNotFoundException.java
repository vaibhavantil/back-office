package com.hedvig.backoffice.config.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExternalServiceNotFoundException extends HystrixBadRequestException {
    public ExternalServiceNotFoundException(String message) {
        super(message);
    }
}
