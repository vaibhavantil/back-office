package com.hedvig.backoffice.services.claims;

public class ClaimException extends Exception {

    public ClaimException(String msg) {
        super(msg);
    }

    public ClaimException(Throwable t) {
        super(t);
    }

}
