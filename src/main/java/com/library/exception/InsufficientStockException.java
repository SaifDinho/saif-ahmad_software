package com.library.exception;

public class InsufficientStockException extends LibraryException {
    private static final long serialVersionUID = 1L;

    public InsufficientStockException(String message) {
        super(message);
    }
}
