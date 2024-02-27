package com.wenthor.invitationservice.service;

import org.springframework.http.HttpHeaders;

import java.util.UUID;

public interface IServiceMain<BO>{
    BO create(HttpHeaders headers, BO bo);
    BO findByID(UUID id);
    BO updateByID(HttpHeaders headers, UUID id, BO bo);
    BO deleteByID(UUID id);
}
