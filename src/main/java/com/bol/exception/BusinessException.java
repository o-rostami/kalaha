package com.bol.exception;


/**
 * A<i>BusinessException</i>.This Exception will be thrown whenever business implementation
 * face exception<p>
 *
 * @author Omid Rostami
 */

public class BusinessException extends BaseException {

    public BusinessException(String message, String... args) {
        super(message);
        setArguments(args);
    }

    public BusinessException() {
    }
}
