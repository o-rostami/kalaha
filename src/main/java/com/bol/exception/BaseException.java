package com.bol.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {

    private String[] arguments;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException() {
    }

}
