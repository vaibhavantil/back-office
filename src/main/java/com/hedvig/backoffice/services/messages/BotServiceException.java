package com.hedvig.backoffice.services.messages;

public class BotServiceException extends Exception {

    public BotServiceException(Throwable exception) {
        super(exception);
    }

    public BotServiceException(String msg) {
        super(msg);
    }

}
