package com.wenthor.response;

public record RestResponse <T>(
        T payload,
        boolean isSuccess,
        String responseDate
) {
}
