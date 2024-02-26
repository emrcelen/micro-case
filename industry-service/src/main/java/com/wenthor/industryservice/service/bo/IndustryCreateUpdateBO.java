package com.wenthor.industryservice.service.bo;

import java.util.Set;
import java.util.UUID;

public record IndustryCreateUpdateBO(
        String name,
        UUID parentIndustry,
        Set<UUID> subIndustries
) {
}
