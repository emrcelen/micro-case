package com.wenthor.industryservice.exception;

public class IndustryNameNotAlphaNumericException extends RuntimeException{
    public IndustryNameNotAlphaNumericException() {
        super("The industry name should consist of only letters and numbers.");
    }
}
