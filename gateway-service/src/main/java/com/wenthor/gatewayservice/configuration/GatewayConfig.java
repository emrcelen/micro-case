package com.wenthor.gatewayservice.configuration;

import com.wenthor.gatewayservice.security.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {
    private final AuthenticationFilter authenticationFilter;

    public GatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://user-service"))
                .route(r -> r.path("/industry-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://industry-service"))
                .route(r -> r.path("/invitation-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://invitation-service"))
                .route("user-service", r -> r.path(
                                "/login",
                                "/register",
                                "/user/**",
                                "/validate/**",
                                "/refresh")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://user-service"))
                .route("industry-service", r-> r.path("/industry/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://industry-service"))
                .route("invitation-service", r-> r.path("/invitation/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://invitation-service"))
                .build();
    }
}
