package com.wenthor.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Verification Controller", description = "A controller providing endpoints for user account verification and code request functionalities.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public interface VerificationControllerSwagger {
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "You can verify your account using this endpoint.")
    @GetMapping(path = "/validate/{code}")
    ResponseEntity<?> validate(@PathVariable(name = "code") String code);
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "You can request a new code to verify your account using this endpoint.")
    @PostMapping(path = "/refresh")
    ResponseEntity<?> refresh(@RequestHeader HttpHeaders headers);
}
