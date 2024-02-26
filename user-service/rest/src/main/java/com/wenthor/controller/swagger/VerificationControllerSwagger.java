package com.wenthor.controller.swagger;

import com.wenthor.dto.request.user.LoginUser;
import com.wenthor.dto.request.user.RegisterUser;
import com.wenthor.dto.request.user.UpdateUser;
import com.wenthor.dto.response.rest.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

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
    @PostMapping(path = "/validate/{code}")
    ResponseEntity<?> validate(@RequestHeader HttpHeaders headers, @PathVariable(name = "code") String code);
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "You can request a new code to verify your account using this endpoint.")
    @PostMapping(path = "/refresh")
    ResponseEntity<?> refresh(@RequestHeader HttpHeaders headers);
}
