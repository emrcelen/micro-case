package com.wenthor.invitationservice.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InvitationCreateRequest(
        @Schema(example = "This is a description.")
        @NotEmpty(message = "You cannot leave this message field empty.")
        @NotNull(message = "You cannot leave this message field null.")
        @NotBlank(message = "You cannot leave this message field blank.")
        String message,
        @NotEmpty(message = "You cannot leave this user field empty.")
        @NotNull(message = "You cannot leave this user field null.")
        @NotBlank(message = "You cannot leave this user field blank.")
        UUID user
) {
}
