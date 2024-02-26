package com.wenthor.bo;

import com.wenthor.enumeration.Role;
import com.wenthor.enumeration.Status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserBO(
        UUID id,
        String fullName,
        String normalizedName,
        Status status,
        String email,
        String password,
        Role role,
        Set<UUID> industries,
        boolean accountExpired,
        boolean accountLock,
        boolean credentialSlock,
        boolean enabled,
        LocalDateTime created,
        LocalDateTime updated,
        UUID createdBy,
        UUID updatedBy
) {
}
