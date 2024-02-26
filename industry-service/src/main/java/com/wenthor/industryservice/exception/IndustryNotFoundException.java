package com.wenthor.industryservice.exception;

public class IndustryNotFoundException extends RuntimeException{
    public IndustryNotFoundException(String message) {
        super(message);
    }
}
