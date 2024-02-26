package com.wenthor.industryservice.exception;

public class IndustryNameNotUniqueException extends RuntimeException{
    public IndustryNameNotUniqueException(String industryName) {
        super(String.format("The industry name '%s' is not unique. Please choose another name.",industryName));
    }
}
