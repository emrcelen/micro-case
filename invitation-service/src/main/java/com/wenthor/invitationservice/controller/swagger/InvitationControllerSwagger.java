package com.wenthor.invitationservice.controller.swagger;

import com.wenthor.invitationservice.controller.dto.request.InvitationCreateRequest;
import com.wenthor.invitationservice.controller.dto.request.InvitationUpdateRequest;
import com.wenthor.invitationservice.controller.dto.response.rest.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Tag(name = "Invitation Controller", description = "A controller containing endpoints for basic CRUD operations on Invitation.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public interface InvitationControllerSwagger {
    @Operation(summary = "You can use this endpoint to create a new invitation.",
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
            })
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> create(@Valid
                             @RequestHeader HttpHeaders headers,
                             @RequestBody InvitationCreateRequest request);

    @Operation(summary = "This endpoint allows you to search for a invitation by ID.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> findByID(@PathVariable(name = "id") UUID id);

    @Operation(summary = "This endpoint allows you to update for a invitation by ID.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> updateByID(@Valid
                                 @RequestHeader HttpHeaders headers,
                                 @PathVariable(name = "id") UUID id,
                                 @RequestBody InvitationUpdateRequest request);

    @Operation(summary = "This endpoint allows you to dekete for a invitation by ID.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> deleteByID(@PathVariable(name = "id") UUID id);

    @Operation(summary = "This endpoint returns all invitations belonging to the user.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> findAllByUserID(@PathVariable(name = "id") UUID id);

    @Operation(summary = "This endpoint is used to indicate that participation will be provided to the invited invitation.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> acceptInvitation(@RequestHeader HttpHeaders headers,
                                       @PathVariable(name = "code") String invitationCode);

    @Operation(summary = "This endpoint is used to request additional time to participate in past invitations.",
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
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> refreshInvitation(@RequestHeader HttpHeaders headers,
                                        @PathVariable(name = "id") UUID invitation);
}
