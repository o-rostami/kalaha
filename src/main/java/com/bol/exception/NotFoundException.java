package com.bol.exception;


/**
 * A<i>NotFoundException</i>.This Exception will be thrown whenever specified objects do not exist<p>
 *
 * @author Omid Rostami
 */

public class NotFoundException extends BaseException {

    public NotFoundException(String message, String... args) {
        super(message);
        setArguments(args);
    }

    public NotFoundException() {
    }
}
