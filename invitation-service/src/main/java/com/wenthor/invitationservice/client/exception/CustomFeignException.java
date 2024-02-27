package com.wenthor.invitationservice.client.exception;

public class CustomFeignException extends RuntimeException {
    private final String errorMessage;
    private final String detail;
    private final String errorDate;
    public CustomFeignException(String message, String detail, String errorDate) {
        super(message);
        this.errorMessage = message;
        this.detail = detail;
        this.errorDate = errorDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public String getDetail() {
        return detail;
    }
    public String getErrorDate() {
        return errorDate;
    }
}
