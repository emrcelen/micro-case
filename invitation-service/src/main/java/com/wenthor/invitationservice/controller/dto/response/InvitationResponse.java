package com.wenthor.invitationservice.controller.dto.response;

import com.wenthor.invitationservice.enumeration.Status;

import java.util.UUID;

public record InvitationResponse(
        UUID id,
        String message,
        String code,
        Status status,
        UUID user
) {
}
