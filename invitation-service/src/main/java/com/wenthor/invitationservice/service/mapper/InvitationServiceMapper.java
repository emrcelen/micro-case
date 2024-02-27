package com.wenthor.invitationservice.service.mapper;

import com.wenthor.invitationservice.model.Invitation;
import com.wenthor.invitationservice.service.bo.InvitationBO;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


public class InvitationServiceMapper {
    public static Invitation convertToPO(InvitationBO bo){
        return new Invitation.Builder()
                .id(bo.id())
                .message(bo.message())
                .code(bo.code())
                .status(bo.status())
                .user(bo.user())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .createdBy(bo.createdBy())
                .updatedBy(bo.updatedBy())
                .build();
    }
    public static InvitationBO convertToBO(Invitation invitation){
        return new InvitationBO(
                invitation.getId(),
                invitation.getMessage(),
                invitation.getCode(),
                invitation.getStatus(),
                invitation.getUser(),
                invitation.getCreated(),
                invitation.getUpdated(),
                invitation.getExpiredDate(),
                invitation.getCreatedBy(),
                invitation.getUpdatedBy()
        );
    }
    public static List<InvitationBO> convertToBO(List<Invitation> list){
        return list.stream()
                .map(InvitationServiceMapper::convertToBO)
                .collect(Collectors.toList());
    }
}
