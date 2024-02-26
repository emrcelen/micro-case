package com.wenthor.industryservice.exception.handler;

import com.wenthor.industryservice.client.exception.CustomFeignException;
import com.wenthor.industryservice.controller.dto.response.rest.RestInvalidResponse;
import com.wenthor.industryservice.controller.dto.response.rest.RestResponse;
import com.wenthor.industryservice.controller.mapper.RestResponseMapper;
import com.wenthor.industryservice.exception.IndustryNameNotAlphaNumericException;
import com.wenthor.industryservice.exception.IndustryNameNotUniqueException;
import com.wenthor.industryservice.exception.IndustryNotFoundException;
import com.wenthor.industryservice.exception.UserAlreadySubscribedException;
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
    @ExceptionHandler(IndustryNameNotAlphaNumericException.class)
    private ResponseEntity<?> handleIndustryNameNotAlphaNumericException(IndustryNameNotAlphaNumericException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IndustryNameNotUniqueException.class)
    private ResponseEntity<?> handleIndustryNameNotUniqueException(IndustryNameNotUniqueException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IndustryNotFoundException.class)
    private ResponseEntity<?> handleIndustryNotFoundException(IndustryNotFoundException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserAlreadySubscribedException.class)
    private ResponseEntity<?> handleUserAlreadySubscribedException(UserAlreadySubscribedException ex, WebRequest request){
        final String message = ex.getMessage();
        final String detail = request.getDescription(false);
        return getResponse(message,detail,HttpStatus.BAD_REQUEST);
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
