package com.hedvig.backoffice.services.messages;

public class BotServiceException extends Exception {

    private int code;

    public BotServiceException(Throwable exception) {
        super(exception);
        code = 500;
    }

    public BotServiceException(String msg) {
        super(msg);
        code = 500;
    }

    public BotServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
