package com.wenthor.industryservice.service.mapper;

import com.wenthor.industryservice.model.Industry;
import com.wenthor.industryservice.service.bo.BasicIndustryBO;
import com.wenthor.industryservice.service.bo.IndustryBO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IndustryServiceMapper {
    public static final IndustryBO convertToBO(Industry industry){
        return new IndustryBO(
                industry.getId(),
                industry.getCreated(),
                industry.getUpdated(),
                industry.getCreatedBy(),
                industry.getUpdatedBy(),
                industry.getName(),
                industry.getNormalizedName(),
                industry.getParentIndustry() != null ? getIndustryBO(industry.getParentIndustry()) : null,
                !industry.getSubIndustries().isEmpty() ? getIndustryBO(industry.getSubIndustries()) : new ArrayList<>(),
                industry.getUsers()
        );
    }

    private static BasicIndustryBO getIndustryBO(Industry industry) {
        return new BasicIndustryBO(industry.getId(),industry.getName());
    }
    private static List<BasicIndustryBO> getIndustryBO(List<Industry> industries){
        return industries.stream()
                .map(IndustryServiceMapper::getIndustryBO)
                .collect(Collectors.toList());
    }

    public static final Industry convertToPO(IndustryBO industryBO){
        return new Industry.Builder()
                .updatedDate(industryBO.updated())
                .createdBy(industryBO.createdBy())
                .updatedBy(industryBO.updatedBy())
                .name(industryBO.name())
                .normalizedName(industryBO.normalizedName())
                .parentIndustry(null)
                .subIndustries(new ArrayList<>())
                .users(industryBO.users())
                .build();
    }
}
