package com.wenthor.industryservice.controller.swagger;

import com.wenthor.industryservice.controller.dto.request.IndustryCreateDTO;
import com.wenthor.industryservice.controller.dto.request.IndustryUpdateDTO;
import com.wenthor.industryservice.controller.dto.response.rest.RestResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Industry Controller", description = "A controller containing endpoints for basic CRUD operations on Industries.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public interface IndustryControllerSwagger {
    @Operation(summary = "You can use this endpoint to create a new industry.",
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
                             @RequestBody IndustryCreateDTO request);

    @Operation(summary = "This endpoint allows you to search for a industry by ID.",
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
    ResponseEntity<?> findByID(UUID id);

    @Operation(summary = "This endpoint returns all users under the specified industry identifier.",
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
    ResponseEntity<?> getAllUsersUnderIndustry(UUID id);

    @Operation(summary = "This endpoint returns all sub-industries under the specified industry identifier.",
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
    ResponseEntity<?> getAllSubIndustries(UUID id);

    @Operation(summary = "This endpoint allows you to update for a industry by ID.",
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
                                 @RequestBody IndustryUpdateDTO request);

    @Operation(summary = "This endpoint allows you to update the parent or sub-industry using the specified industry identifier",
            parameters = {
                    @Parameter(name = "parent"),
                    @Parameter(name = "sub")
            },
            responses= {
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
    ResponseEntity<?> updateIndustryHierarchy(@RequestHeader HttpHeaders headers,
                                              @PathVariable(name = "id") UUID mainIndustry,
                                              @RequestParam(name = "parent", required = false) UUID parentIndustry,
                                              @RequestParam(name = "sub", required = false)UUID subIndustry);

    @Operation(summary = "This endpoint allows you to delete for a industry by ID.",
            responses = {
                    @ApiResponse(responseCode = "204")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> deleteByID(UUID id);

    @Operation(summary = "This endpoint initiates a user subscription based on the desired industry.",
            parameters = {
                    @Parameter(name = "user")
            },
            responses= {
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
    ResponseEntity<?> subsribeIndustry(@RequestHeader HttpHeaders headers,
                                       @PathVariable(name = "industry") UUID industryID,
                                       @RequestParam(name = "user") UUID userID);

    @Operation(summary = "This endpoint cancels a user subscription based on the desired industry.",
            parameters = {
                    @Parameter(name = "user")
            },
            responses= {
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
    ResponseEntity<?> unSubsribeIndustry(@RequestHeader HttpHeaders headers,
                                       @PathVariable(name = "industry") UUID industryID,
                                       @RequestParam(name = "user") UUID userID);

}
