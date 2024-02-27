package com.wenthor.gatewayservice.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.List;

@Component
public class RouterValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/register",  // Kayıt işlemi
            "/login",     // Giriş işlemi
            "/validate"
    );

    public static Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
