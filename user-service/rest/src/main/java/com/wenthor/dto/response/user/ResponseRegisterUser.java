package com.wenthor.dto.response.user;

public record ResponseRegisterUser(
        String fullName,
        String normalizedName,
        String email
) {
}
