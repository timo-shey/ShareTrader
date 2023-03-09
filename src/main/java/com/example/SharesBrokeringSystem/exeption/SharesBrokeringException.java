package com.example.SharesBrokeringSystem.exeption;

import org.springframework.http.HttpStatus;

public class SharesBrokeringException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public SharesBrokeringException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public SharesBrokeringException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SharesBrokeringException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public SharesBrokeringException(String notEnoughSharesToSell) {
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
