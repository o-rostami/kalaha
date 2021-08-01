package com.bol.exception;

public class NotNullException extends BaseException {

    public NotNullException(String message, String ...args) {
        super(message);
        setArguments(args);
    }

    public NotNullException() {
    }
}
