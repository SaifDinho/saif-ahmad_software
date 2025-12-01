package com.library.exception;

public class UserNotFoundException extends LibraryException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
