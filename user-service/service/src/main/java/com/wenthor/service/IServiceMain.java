package com.wenthor.service;

import java.util.UUID;

public interface IServiceMain <BO>{

    default BO create(BO bo){
        throw new UnsupportedOperationException("This operation is not supported.");
    };
    BO findByID(UUID id);
    default BO updateByID(String token, UUID uuid, BO bo) {
        throw new UnsupportedOperationException("This operation is not supported.");
    };;
    BO deleteByID(UUID uuid);
}
