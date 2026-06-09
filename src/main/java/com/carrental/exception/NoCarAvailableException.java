package com.carrental.exception;

public class NoCarAvailableException extends RuntimeException {

    public NoCarAvailableException() {
        super("No car available for the requested period");
    }

    public NoCarAvailableException(String message) {
        super(message);
    }

    public NoCarAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
