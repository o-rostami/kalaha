package com.bol.exception;

public class BusinessException extends BaseException {

    public BusinessException(String message, String ...args) {
        super(message);
        setArguments(args);
    }

    public BusinessException() {
    }
}
