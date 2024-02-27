package com.wenthor.invitationservice.controller;

import com.wenthor.invitationservice.controller.dto.request.InvitationCreateRequest;
import com.wenthor.invitationservice.controller.dto.request.InvitationUpdateRequest;
import com.wenthor.invitationservice.controller.mapper.InvitationControllerMapper;
import com.wenthor.invitationservice.controller.mapper.RestResponseMapper;
import com.wenthor.invitationservice.controller.swagger.InvitationControllerSwagger;
import com.wenthor.invitationservice.service.InvitationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/invitation")
public class InvitationController implements InvitationControllerSwagger {
    private final InvitationService service;

    public InvitationController(InvitationService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(HttpHeaders headers, InvitationCreateRequest request) {
        var requestBO = InvitationControllerMapper.convertToBO(request);
        var saveBO = this.service.create(headers,requestBO);
        var payload = InvitationControllerMapper.convertToResponse(saveBO);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findByID(UUID id) {
        var bo = this.service.findByID(id);
        var payload = InvitationControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateByID(HttpHeaders headers, UUID id, InvitationUpdateRequest request) {
        var requestBO = InvitationControllerMapper.convertToBO(request);
        var saveBO = this.service.updateByID(headers,id,requestBO);
        var payload = InvitationControllerMapper.convertToResponse(saveBO);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteByID(UUID id) {
        var bo = this.service.deleteByID(id);
        var payload = InvitationControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(path = "/{id}/all")
    public ResponseEntity<?> findAllByUserID(UUID id) {
        var bo = this.service.findAllByUserID(id);
        var payload = InvitationControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping(path = "/accept/{code}")
    public ResponseEntity<?> acceptInvitation(HttpHeaders headers, String invitationCode) {
        var bo = this.service.acceptInvitation(headers,invitationCode);
        var payload = InvitationControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping(path = "/refresh/{id}")
    public ResponseEntity<?> refreshInvitation(HttpHeaders headers, UUID invitation) {
        var bo = this.service.refreshInvitation(headers,invitation);
        var payload = InvitationControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(payload);
        return ResponseEntity.ok(response);
    }
}
