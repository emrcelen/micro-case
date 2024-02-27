package com.wenthor.invitationservice.exception.handler;


import com.wenthor.invitationservice.client.exception.CustomFeignException;
import com.wenthor.invitationservice.controller.dto.response.rest.RestInvalidResponse;
import com.wenthor.invitationservice.controller.dto.response.rest.RestResponse;
import com.wenthor.invitationservice.controller.mapper.RestResponseMapper;
import com.wenthor.invitationservice.exception.InvitationExpiredException;
import com.wenthor.invitationservice.exception.InvitationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvitationExpiredException.class)
    private ResponseEntity<?> handleInvitationExpiredException(InvitationExpiredException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvitationNotFoundException.class)
    private ResponseEntity<?> handleInvitationNotFoundException(InvitationNotFoundException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        final String message = ex.getTypeMessageCode();
        final String detail = request.getDescription(false);
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return getResponse(message,detail,errors,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomFeignException.class)
    private ResponseEntity<?> handleCustomFeignException(CustomFeignException ex){
        final RestInvalidResponse response = new RestInvalidResponse<>(ex.getErrorMessage(),null,ex.getDetail(),ex.getErrorDate());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> handleGeneralException(Exception ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
    }
    private ResponseEntity<?> getResponse(String message, String detail, HttpStatus httpStatus){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        RestInvalidResponse response = new RestInvalidResponse(
                message,
                null,
                detail,
                formatter.format(LocalDateTime.now())
        );
        RestResponse<RestInvalidResponse> restResponse = RestResponseMapper.convertToNotSuccessResponse(response);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
    private ResponseEntity<?> getResponse(String message, String detail, Map<String, String> errors, HttpStatus httpStatus){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        RestInvalidResponse response = new RestInvalidResponse(
                message,
                errors,
                detail,
                formatter.format(LocalDateTime.now())
        );
        RestResponse<RestInvalidResponse> restResponse = RestResponseMapper.convertToNotSuccessResponse(response);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
}
