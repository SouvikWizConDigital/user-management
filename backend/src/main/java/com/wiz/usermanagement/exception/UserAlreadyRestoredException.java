package com.wiz.usermanagement.exception;

public class UserAlreadyRestoredException extends RuntimeException {
    public UserAlreadyRestoredException(String message) {
        super(message);
    }
}
