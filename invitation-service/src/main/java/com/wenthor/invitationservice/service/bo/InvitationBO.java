package com.wenthor.invitationservice.service.bo;

import com.wenthor.invitationservice.enumeration.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvitationBO(
        UUID id,
        String message,
        String code,
        Status status,
        UUID user,
        LocalDateTime created,
        LocalDateTime updated,
        LocalDateTime expired,
        UUID createdBy,
        UUID updatedBy
) {
}
