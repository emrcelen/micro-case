package com.wenthor.industryservice.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record IndustryCreateDTO(
        @Schema(example = "ExampleCompany2")
        @NotEmpty(message = "You cannot leave this name field empty.")
        @NotNull(message = "You cannot leave this name field null.")
        @NotBlank(message = "You cannot leave this name field blank.")
        @Pattern(regexp = "[a-zA-Z0-9]+$", message = "Please enter a valid industry name address.")
        String name
) {
}
