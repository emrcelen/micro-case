package com.wenthor.gatewayservice.response;

import java.net.URI;

public record ErrorResponse(
        String errorMessage,
        URI detail,
        boolean isSuccess,
        String responseDate
) {
}
