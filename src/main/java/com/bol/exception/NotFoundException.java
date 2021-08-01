package com.bol.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(String message, String ...args) {
        super(message);
        setArguments(args);
    }

    public NotFoundException() {
    }
}
