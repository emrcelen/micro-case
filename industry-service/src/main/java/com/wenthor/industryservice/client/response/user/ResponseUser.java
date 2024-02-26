package com.wenthor.industryservice.client.response.user;


import com.wenthor.industryservice.client.response.enums.Status;

import java.util.UUID;

public record ResponseUser(
        UUID id,
        String fullName,
        String normalizedName,
        String email,
        Status status,
        String createdTime,
        String updatedTime,
        UUID createdBy,
        UUID updatedBy
) {
}
