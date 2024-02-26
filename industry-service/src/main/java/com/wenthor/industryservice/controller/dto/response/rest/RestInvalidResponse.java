package com.wenthor.industryservice.controller.dto.response.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

public record RestInvalidResponse <T>(
        String errorMessage,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T errors,
        String detail,
        String errorDate
) {
}