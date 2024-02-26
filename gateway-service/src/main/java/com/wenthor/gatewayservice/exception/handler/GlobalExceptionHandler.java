package com.wenthor.gatewayservice.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenthor.gatewayservice.exception.AuthException;
import com.wenthor.gatewayservice.response.ErrorResponse;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof AuthException)
            return handleAuthException((AuthException) ex, exchange);
        else if (ex instanceof SignatureException)
            return handleSignatureException((SignatureException) ex, exchange);
        else
            return handleGeneralException((Exception) ex, exchange);
    }

    private Mono<Void> writeResponse(ResponseEntity<?> response, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(response.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Object body = response.getBody();
        if (body instanceof Flux) {
            return exchange.getResponse().writeWith(((Flux<?>) body)
                    .flatMap(obj -> {
                        try {
                            byte[] bytes = new ObjectMapper().writeValueAsBytes(obj);
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return Mono.just(buffer);
                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    }));
        } else if (body instanceof Mono) {
            return exchange.getResponse().writeWith(((Mono<?>) body)
                    .flatMap(obj -> {
                        try {
                            byte[] bytes = new ObjectMapper().writeValueAsBytes(obj);
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return Mono.just(buffer);
                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    }));
        } else {
            try {
                byte[] bytes = new ObjectMapper().writeValueAsBytes(body);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                return exchange.getResponse().writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        }
        }

    private Mono<Void> handleSignatureException(SignatureException ex, ServerWebExchange exchange) {
        final String message = ex.getMessage();
        final URI uri = exchange.getRequest().getURI();
        return getResponse(message, uri, HttpStatus.UNAUTHORIZED)
                .flatMap(response -> writeResponse(response, exchange));
    }

    private Mono<Void> handleAuthException(AuthException ex, ServerWebExchange exchange) {
        final String message = ex.getMessage();
        final URI uri = exchange.getRequest().getURI();
        return getResponse(message, uri, HttpStatus.UNAUTHORIZED)
                .flatMap(response -> writeResponse(response, exchange));
    }

    private Mono<Void> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        final String message = ex.getMessage();
        final URI uri = exchange.getRequest().getURI();
        return getResponse(message, uri, HttpStatus.UNAUTHORIZED)
                .flatMap(response -> writeResponse(response, exchange));
    }

    private Mono<ResponseEntity<?>> getResponse(String message, URI uri, HttpStatus status) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ErrorResponse response = new ErrorResponse(
                message,
                uri,
                false,
                formatter.format(LocalDateTime.now())
        );
        return Mono.just(ResponseEntity.status(status)
                .body(response));
    }
}
