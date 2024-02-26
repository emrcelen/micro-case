package com.wenthor.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseLoginUser(
        @Schema(example = "demo@jwt.com")
        String email,
        @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW1vQGp3dC5jb20iLCJpYXQiOjE3MDg4MDgzNzgsImV4cCI6MTcwODgyMjc3OH0.PmjS1nQdccSlcWBiUYNzX2wh-lTo57UV-SigDLctl3E")
        String token,
        @Schema(example = "1708808378")
        long tokenIssuedTime,
        @Schema(example = "1708822778")
        long tokenExpirationTime
) {
}
