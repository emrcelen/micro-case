package com.wenthor.dto.response.rest;

import com.wenthor.dto.response.user.ResponseLoginUser;
import com.wenthor.dto.response.user.ResponseRegisterUser;
import com.wenthor.dto.response.user.ResponseUser;
import io.swagger.v3.oas.annotations.media.Schema;

public record RestResponse <T>(
        @Schema(oneOf = {ResponseLoginUser.class, ResponseRegisterUser.class, ResponseUser.class})
        T payload,
        boolean isSuccess,
        @Schema(example ="25/02/2024 01:45:37")
        String responseDate
) {
}
