package com.wenthor.service;

import com.wenthor.bo.UserBO;
import com.wenthor.bo.VerificationCodeBO;
import com.wenthor.enumeration.Status;
import com.wenthor.exception.UnexpectedException;
import com.wenthor.exception.VerificationCodeExpiredException;
import com.wenthor.exception.VerificationCodeNotFoundException;
import com.wenthor.mapper.UserServiceMapper;
import com.wenthor.mapper.VerificationCodeServiceMapper;
import com.wenthor.model.VerificationCode;
import com.wenthor.repository.VerificationCodeRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class VerificationCodeService implements IServiceMain<VerificationCodeBO> {
    private final VerificationCodeRepository repository;
    private final UserService userService;
    private final JwtService jwtService;

    public VerificationCodeService(VerificationCodeRepository repository,
                                   @Lazy UserService userService, JwtService jwtService) {
        this.repository = repository;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    public VerificationCodeBO create(UserBO userBO) {
        if (hasVerificationCode(userBO.id())) {
            VerificationCode po = this.repository.findByUserId(userBO.id())
                    .orElseThrow(
                            () -> new UnexpectedException("An unexpected error occurred. Please contact the system administrator.")
                    );
            this.repository.delete(po);
            VerificationCode newPo = generate(userBO);
            VerificationCode savePO = this.repository.save(newPo);
            return VerificationCodeServiceMapper.convertToBO(savePO);
        } else {
            VerificationCode po = generate(userBO);
            VerificationCode savePO = this.repository.save(po);
            return VerificationCodeServiceMapper.convertToBO(savePO);
        }
    }

    public void verification(String token, String code) {
        String email = this.jwtService.findByAccountEmail(token);
        VerificationCode codePO = this.repository.findByCode(code)
                .orElseThrow(
                        () -> new VerificationCodeNotFoundException(
                                String.format(
                                        "The verification code (%s) you are looking for could not be found in the system."
                                        , code
                                )
                        )
                );
        if(!codePO.getUser().getEmail().equals(email))
            throw new IllegalArgumentException("Your request has been rejected.");
        if (!codePO.getExpiryDate().isAfter(LocalDateTime.now())) {
            this.delete(codePO);
            throw new VerificationCodeExpiredException("Verification code has expired.");
        }
        codePO.getUser().setEnabled(false);
        codePO.getUser().setStatus(Status.ACTIVE);
        this.userService.update(UserServiceMapper.convertToBO(codePO.getUser()));
        this.delete(codePO);
    }

    public void refreshCode(String token) {
        String email = this.jwtService.findByAccountEmail(token);
        UserBO userBO = this.userService.findByEmail(email);
        if (userBO.enabled())
            this.create(userBO);
        else
            throw new IllegalArgumentException("Your request has been rejected.");
    }

    @Override
    public VerificationCodeBO findByID(UUID id) {
        if (id != null) {
            VerificationCode codePO = this.repository.findById(id).orElseThrow(
                    () -> new VerificationCodeNotFoundException(
                            String.format(
                                    "The verification code (id: %s) you are looking for could not be found in the system."
                                    , id.toString()
                            )
                    )
            );
            VerificationCodeBO codeBO = VerificationCodeServiceMapper.convertToBO(codePO);
            return codeBO;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    @Override
    public VerificationCodeBO deleteByID(UUID id) {
        if (id != null) {
            VerificationCodeBO bo = this.findByID(id);
            VerificationCode po = VerificationCodeServiceMapper.convertToPO(bo);
            this.repository.delete(po);
            return bo;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    private void delete(VerificationCode code) {
        this.repository.delete(code);
    }

    private VerificationCode generate(UserBO userBO) {
        VerificationCode po = new VerificationCode.Builder()
                .id(UUID.randomUUID())
                .code(codeGenarator(userBO.email()))
                .user(UserServiceMapper.convertToPO(userBO))
                .createdBy(userBO.id())
                .updatedBy(userBO.id())
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        return po;
    }

    private String codeGenarator(String userMail) {
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        final String hash = userMail.concat("_").concat(date);

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(hash.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            return UUID.randomUUID().toString();
        }
    }

    private boolean hasVerificationCode(UUID uuid) {
        return this.repository.findByUserId(uuid).isPresent();
    }
}
