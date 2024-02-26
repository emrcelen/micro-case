package com.wenthor.industryservice.controller;

import com.wenthor.industryservice.controller.dto.request.IndustryCreateDTO;
import com.wenthor.industryservice.controller.dto.request.IndustryUpdateDTO;
import com.wenthor.industryservice.controller.mapper.IndustryControllerMapper;
import com.wenthor.industryservice.controller.mapper.RestResponseMapper;
import com.wenthor.industryservice.controller.swagger.IndustryControllerSwagger;
import com.wenthor.industryservice.service.IndustryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/industry")
public class IndustryController implements IndustryControllerSwagger {
    private final IndustryService service;

    public IndustryController(IndustryService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(HttpHeaders headers, IndustryCreateDTO request) {
        var convert = IndustryControllerMapper.convertToCreateBO(request);
        var bo = this.service.create(headers, convert);
        var dto = IndustryControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findByID(@PathVariable(name = "id") UUID id) {
        var bo = this.service.findByID(id);
        var dto = IndustryControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(path = "/{id}/users")
    public ResponseEntity<?> getAllUsersUnderIndustry(@PathVariable(name = "id") UUID id) {
        var bo = this.service.findByID(id);
        var dto = IndustryControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(dto.users());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(path = "/{id}/all")
    public ResponseEntity<?> getAllSubIndustries(@PathVariable(name = "id") UUID id) {
        var bo = this.service.findByID(id);
        var dto = IndustryControllerMapper.convertToResponse(bo);
        var response = RestResponseMapper.convertToSuccessResponse(dto.subIndustries());
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateByID(HttpHeaders headers, UUID id,
                                        IndustryUpdateDTO request) {
        var convert = IndustryControllerMapper.convertToUpdateBO(request);
        var bo = this.service.updateByID(headers, id, convert);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateIndustryHierarchy(HttpHeaders headers, UUID mainIndustry,
                                                     UUID parentIndustry, UUID subIndustry) {
        var bo = this.service.updateIndustryHierarchy(headers,mainIndustry,parentIndustry,subIndustry);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable(name = "id") UUID id) {
        this.service.deleteByID(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping(path = "/{industry}/subscribe")
    public ResponseEntity<?> subsribeIndustry(HttpHeaders headers, UUID industryID, UUID userID) {
        var bo = this.service.subscribeIndustry(headers,industryID,userID);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping(path = "/{industry}/unsubscribe")
    public ResponseEntity<?> unSubsribeIndustry(HttpHeaders headers, UUID industryID, UUID userID) {
        var bo = this.service.unSubscribeIndustry(headers,industryID,userID);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }

}
