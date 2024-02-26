package com.wenthor.mapper;

import com.wenthor.bo.VerificationCodeBO;
import com.wenthor.model.VerificationCode;

public class VerificationCodeServiceMapper {
    public static VerificationCodeBO convertToBO(VerificationCode code) {
        return new VerificationCodeBO(
                code.getId(),
                code.getCode(),
                UserServiceMapper.convertToBO(code.getUser()),
                code.getExpiryDate(),
                code.getBaseEntity().getCreated(),
                code.getBaseEntity().getUpdated(),
                code.getBaseEntity().getCreatedBy(),
                code.getBaseEntity().getUpdatedBy()
        );
    }

    public static VerificationCode convertToPO(VerificationCodeBO codeBO) {
        return new VerificationCode.Builder()
                .id(codeBO.id())
                .code(codeBO.code())
                .user(UserServiceMapper.convertToPO(codeBO.userBO()))
                .expiryDate(codeBO.expiryDate())
                .createdBy(codeBO.createdBy())
                .updatedBy(codeBO.updatedBy())
                .build();
    }

}
