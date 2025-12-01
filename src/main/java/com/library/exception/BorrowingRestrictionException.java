package com.library.exception;

public class BorrowingRestrictionException extends LibraryException {
    private static final long serialVersionUID = 1L;

    public BorrowingRestrictionException(String message) {
        super(message);
    }
}
