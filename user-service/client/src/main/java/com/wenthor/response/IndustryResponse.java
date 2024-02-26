package com.wenthor.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;
import java.util.UUID;

public record IndustryResponse(
        UUID id,
        String name,
        String normalizedName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID parentIndustry,
        Set<UUID> subIndustries,
        Set<UUID> users
) {
}