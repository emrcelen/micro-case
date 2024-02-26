package com.wenthor.industryservice.service;

import org.springframework.http.HttpHeaders;

import java.util.UUID;

public interface IServiceMain <R,BO>{
    R create(HttpHeaders headers, BO bo);
    R findByID(UUID id);
    R updateByID(HttpHeaders headers, UUID id, BO bo);
    void deleteByID(UUID id);
}
