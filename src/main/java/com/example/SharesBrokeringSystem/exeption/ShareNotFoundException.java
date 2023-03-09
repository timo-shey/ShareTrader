package com.example.SharesBrokeringSystem.exeption;

public class ShareNotFoundException extends RuntimeException{
    public ShareNotFoundException(String message) {
        super(message);
    }
}
