package com.wenthor.mapper;

import com.wenthor.dto.response.rest.RestResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RestResponseMapper {
    public static <T> RestResponse convertToSuccessResponse (T payload){
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return new RestResponse(
                payload,
                true,
                dtf.format(LocalDateTime.now())
        );
    }
    public static <T> RestResponse convertToNotSuccessResponse (T payload){
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return new RestResponse(
                payload,
                false,
                dtf.format(LocalDateTime.now())
        );
    }
}
