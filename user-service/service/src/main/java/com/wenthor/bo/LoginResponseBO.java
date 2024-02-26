package com.wenthor.bo;

public record LoginResponseBO(
        String email,
        String token,
        long tokenIssuedTime,
        long tokenExpirationTime
) {
}
