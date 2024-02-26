package com.wenthor.controller;

import com.wenthor.controller.swagger.VerificationControllerSwagger;
import com.wenthor.service.VerificationCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController implements VerificationControllerSwagger {
    private final VerificationCodeService service;

    public VerificationController(VerificationCodeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<?> validate(@RequestHeader HttpHeaders headers, String code) {
        var token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        this.service.verification(token,code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<?> refresh(@RequestHeader HttpHeaders headers) {
        var token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        this.service.refreshCode(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
