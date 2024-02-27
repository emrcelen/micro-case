package com.wenthor.invitationservice.controller.dto.response.rest;

public record RestResponse <T>(
        T payload,
        boolean isSuccess,
        String responseDate
) {
}
