package com.wenthor.bo;

import java.time.LocalDateTime;
import java.util.UUID;

public record VerificationCodeBO(
        UUID id,
        String code,
        UserBO userBO,
        LocalDateTime expiryDate,
        LocalDateTime created,
        LocalDateTime updated,
        UUID createdBy,
        UUID updatedBy
) {
}
