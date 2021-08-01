package com.bol.exception;

public class FormatException extends BaseException {

    public FormatException(String message, String ...args) {
        super(message);
        setArguments(args);
    }

    public FormatException() {
    }
}
