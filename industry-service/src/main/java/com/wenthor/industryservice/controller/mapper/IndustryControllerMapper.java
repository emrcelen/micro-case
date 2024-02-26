package com.wenthor.industryservice.controller.mapper;

import com.wenthor.industryservice.controller.dto.request.IndustryCreateDTO;
import com.wenthor.industryservice.controller.dto.request.IndustryUpdateDTO;
import com.wenthor.industryservice.controller.dto.response.IndustryResponse;
import com.wenthor.industryservice.service.bo.BasicIndustryBO;
import com.wenthor.industryservice.service.bo.IndustryBO;
import com.wenthor.industryservice.service.bo.IndustryCreateUpdateBO;

import java.util.HashSet;
import java.util.stream.Collectors;

public class IndustryControllerMapper {

    public static IndustryCreateUpdateBO convertToCreateBO(IndustryCreateDTO dto) {
        return new IndustryCreateUpdateBO(
                dto.name(),
                null,
                new HashSet<>()
        );
    }
    public static IndustryCreateUpdateBO convertToUpdateBO(IndustryUpdateDTO dto) {
        return new IndustryCreateUpdateBO(
                dto.name(),
                dto.parentIndustry().orElse(null),
                dto.subIndustries().orElse(new HashSet<>())
        );
    }

    public static IndustryResponse convertToResponse(IndustryBO bo) {
        return new IndustryResponse(
                bo.id(),
                bo.name(),
                bo.normalizedName(),
                bo.parentIndustry() != null ? bo.parentIndustry().id() : null,
                bo.subIndustries().isEmpty() ? new HashSet<>() : bo.subIndustries().stream().map(BasicIndustryBO::id).collect(Collectors.toSet()),
                new HashSet<>(bo.users())
        );
    }
}
