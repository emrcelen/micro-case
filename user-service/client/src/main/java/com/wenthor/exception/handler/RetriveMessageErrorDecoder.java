package com.wenthor.exception.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wenthor.exception.CustomFeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class RetriveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        try {
            System.out.println(errorDecoder.decode(s, response));
            String responseBody = Util.toString(response.body().asReader());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(responseBody);
            String errorMessage = node.path("payload").path("errorMessage").asText();
            String detail = node.path("payload").path("detail").asText();
            String errorDate = node.path("payload").path("errorDate").asText();
            throw new CustomFeignException(errorMessage, detail, errorDate);
        } catch (IOException e) {
            throw new IllegalArgumentException("Your request has been rejected.");
        }
    }
}
