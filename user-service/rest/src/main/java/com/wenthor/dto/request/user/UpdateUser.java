package com.wenthor.dto.request.user;

import com.wenthor.enumeration.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UpdateUser(
        @Schema(example = "abc@wenthor.com")
        @Email(regexp = "^(.+)@(.+)$", message = "Please enter a valid email address.")
        @NotEmpty(message = "You cannot leave this email field empty.")
        @NotNull(message = "You cannot leave this email field null.")
        @NotBlank(message = "You cannot leave this email field blank.")
        String email,
        @Schema(example = "wenthooor")
        @NotEmpty(message = "You cannot leave this password field empty.")
        @NotNull(message = "You cannot leave this password field null.")
        @NotBlank(message = "You cannot leave this password field blank.")
        String password,
        @Schema(example = "I am Groot")
        @NotEmpty(message = "You cannot leave this full name field empty.")
        @NotNull(message = "You cannot leave this full name field null.")
        @NotBlank(message = "You cannot leave this full name field blank.")
        String fullName,
        @Schema(example = "PENDING")
        Status status,
        @Schema(examples = {"71d59163-d2cc-4fad-9852-34dbd8551a92","b8e3c0c3-90ca-47f3-aaca-ed8edc86e30f"})
        Set<UUID> industries,
        boolean accountExpired,
        boolean accountLock,
        boolean credentialSlock) {
}
