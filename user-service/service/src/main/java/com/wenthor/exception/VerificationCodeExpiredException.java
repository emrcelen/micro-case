package com.wenthor.exception;

public class VerificationCodeExpiredException extends RuntimeException{
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}