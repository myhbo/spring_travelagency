package com.training.spring_travelagency.exception;

public class EmailNotUniqueException extends RuntimeException {

    public EmailNotUniqueException() {
    }
    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}