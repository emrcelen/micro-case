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

@Tag(name = "User Controller", description = "A controller containing endpoints for users to create an account, login, and retrieve information about themselves.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public interface UserControllerSwagger {
    @Operation(
            summary = "You can register with this endpoint.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> register(@Valid @RequestBody RegisterUser user);

    @Operation(summary = "You can login with this endpoint.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> login(@Valid @RequestBody LoginUser user);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "This endpoint allows you to search for a user by either ID or email.",
            parameters = {
                    @Parameter(name = "id"),
                    @Parameter(name = "email")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> findByAttribute(@Valid
                                      @RequestParam(name = "id", required = false)
                                      UUID id,
                                      @RequestParam(name = "email", required = false)
                                      @Email(regexp = "^(.+)@(.+)$", message = "Please enter a valid email address.")
                                      String email);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "A paginated endpoint that allows you to access users by providing a normalized name.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> findAll(@Valid
                              @PathVariable(name = "normalized")
                              String normalized,
                              @Positive(message = "Please enter a valid positive page number.")
                              @PathVariable(name = "page")
                              int page,
                              @Positive(message = "Please enter a valid positive page size.")
                              @PathVariable(name = "size")
                              int size);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "This endpoint allows you to delete for a user by either ID or email.",
            parameters = {
                    @Parameter(name = "id"),
                    @Parameter(name = "email")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> deleteByAttribute(@Valid
                                        @RequestParam(name = "id", required = false)
                                        UUID id,
                                        @RequestParam(name = "email", required = false)
                                        @Email(regexp = "^(.+)@(.+)$", message = "Please enter a valid email address.")
                                        String email);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "This endpoint allows you to update for a user by either ID or email.",
            parameters = {
                    @Parameter(name = "id"),
                    @Parameter(name = "email")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> updateByAttribute(@Valid
                                        @RequestHeader
                                        HttpHeaders headers,
                                        @RequestParam(name = "id", required = false)
                                        UUID id,
                                        @RequestParam(name = "email", required = false)
                                        @Email(regexp = "^(.+)@(.+)$", message = "Please enter a valid email address.")
                                        String email,
                                        @RequestBody
                                        UpdateUser updateUser);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "This endpoint initiates a user subscription based on the desired industry.",
            parameters = {
                    @Parameter(name = "industry"),
                    @Parameter(name = "client")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> subscribeIndustry(@RequestHeader
                                        HttpHeaders headers,
                                        @PathVariable(name = "user") UUID id,
                                        @RequestParam(name = "industry") UUID industry,
                                        @RequestParam(name = "client", required = false) boolean control);

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "This endpoint cancels a user subscription based on the desired industry.",
            parameters = {
                    @Parameter(name = "industry"),
                    @Parameter(name = "client")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> unSubscribeIndustry(@RequestHeader
                                          HttpHeaders headers,
                                          @PathVariable(name = "user") UUID id,
                                          @RequestParam(name = "industry") UUID industry,
                                          @RequestParam(name = "client", required = false) boolean control);
}
