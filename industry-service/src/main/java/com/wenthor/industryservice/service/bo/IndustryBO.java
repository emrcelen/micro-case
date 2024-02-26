package com.wenthor.industryservice.service.bo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record IndustryBO(
        UUID id,
        LocalDateTime created,
        LocalDateTime updated,
        UUID createdBy,
        UUID updatedBy,
        String name,
        String normalizedName,
        BasicIndustryBO parentIndustry,
        List<BasicIndustryBO> subIndustries,
        List<UUID> users
) {
}
