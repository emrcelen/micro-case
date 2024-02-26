package com.wenthor.configuration;

import com.wenthor.exception.handler.RetriveMessageErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {
    private static String token;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Authorization", "Bearer " + token);
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetriveMessageErrorDecoder();
    }

    public static void updateJwtSecret(String jwt) {
        token = jwt;
    }
}
