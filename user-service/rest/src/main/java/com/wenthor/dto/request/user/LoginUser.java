package com.wenthor.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginUser(
        @Schema(example = "johndoe@gmail.com")
        @Email(regexp = "^(.+)@(.+)$", message = "Please enter a valid email address.")
        @NotEmpty(message = "You cannot leave this email field empty.")
        @NotNull(message = "You cannot leave this email field null.")
        @NotBlank(message = "You cannot leave this email field blank.")
        String email,
        @Schema(example = "password")
        @NotEmpty(message = "You cannot leave this password field empty.")
        @NotNull(message = "You cannot leave this password field null.")
        @NotBlank(message = "You cannot leave this password field blank.")
        String password
) {
}
