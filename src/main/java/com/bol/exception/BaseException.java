package com.bol.exception;

import lombok.Data;

/**
 * A <i>BaseException</i>. This class is the basic exception class that must be extended by the other exception class<p>
 *
 * @author Omid Rostami
 */


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
