package com.wenthor.invitationservice.controller.mapper;

import com.wenthor.invitationservice.controller.dto.request.InvitationCreateRequest;
import com.wenthor.invitationservice.controller.dto.request.InvitationUpdateRequest;
import com.wenthor.invitationservice.controller.dto.response.InvitationResponse;
import com.wenthor.invitationservice.enumeration.Status;
import com.wenthor.invitationservice.service.bo.InvitationBO;

import java.util.List;
import java.util.stream.Collectors;

public class InvitationControllerMapper {

    public static InvitationResponse convertToResponse(InvitationBO bo){
        return new InvitationResponse(
                bo.id(),
                bo.message(),
                bo.code(),
                bo.status(),
                bo.user()
        );
    }
    public static List<InvitationResponse> convertToResponse(List<InvitationBO> list){
        return list.stream()
                .map(InvitationControllerMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    public static InvitationBO convertToBO(InvitationUpdateRequest request){
        return new InvitationBO(
                null,
                request.message(),
                null,
                request.status(),
                request.user(),
                null,
                null,
                null,
                null,
                null
        );
    }
    public static InvitationBO convertToBO(InvitationCreateRequest request){
        return new InvitationBO(
                null,
                request.message(),
                null,
                Status.PENDING,
                request.user(),
                null,
                null,
                null,
                null,
                null
        );
    }
}
