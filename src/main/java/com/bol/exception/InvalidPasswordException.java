package com.bol.exception;

public class InvalidPasswordException extends BaseException {

    public InvalidPasswordException(String message, String ...args) {
        super(message);
        setArguments(args);
    }

    public InvalidPasswordException() {
    }
}
