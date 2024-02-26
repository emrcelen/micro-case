package com.wenthor.controller;

import com.wenthor.controller.swagger.UserControllerSwagger;
import com.wenthor.dto.request.user.LoginUser;
import com.wenthor.dto.request.user.RegisterUser;
import com.wenthor.dto.request.user.UpdateUser;
import com.wenthor.dto.response.rest.RestResponse;
import com.wenthor.mapper.RestResponseMapper;
import com.wenthor.mapper.UserControllerMapper;
import com.wenthor.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController implements UserControllerSwagger {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(RegisterUser user) {
        var bo = UserControllerMapper.convertRegisterUserToBO(user);
        var po = this.userService.create(bo);
        var response = RestResponseMapper.convertToSuccessResponse(po);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(LoginUser user) {
        var bo = this.userService.login(user.email(), user.password());
        var dto = UserControllerMapper.convertToResponseLoginUser(bo);
        var response = RestResponseMapper.convertToSuccessResponse(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(path = "/user/find")
    public ResponseEntity<?> findByAttribute(UUID id, String email) {
        var bo = this.userService.findByAttribute(id, email);
        var po = UserControllerMapper.convertToResponseUser(bo);
        var response = RestResponseMapper.convertToSuccessResponse(po);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(path = "/user/find/{normalized}/{page}/{size}")
    public ResponseEntity<?> findAll(String normalized,
                                     int page,
                                     int size) {
        var bo = this.userService.findAll(normalized, page, size);
        var po = UserControllerMapper.convertToResponseUsers(bo);
        var response = RestResponseMapper.convertToSuccessResponse(po);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping(path = "/user/delete")
    public ResponseEntity<?> deleteByAttribute(UUID id,
                                               String email) {
        var bo = this.userService.deleteByAttribute(id, email);
        var po = UserControllerMapper.convertToResponseUser(bo);
        var response = RestResponseMapper.convertToSuccessResponse(po);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping(path = "/user/update")
    public ResponseEntity<?> updateByAttribute(HttpHeaders headers,
                                               UUID id,
                                               String email,
                                               UpdateUser updateUser) {
        var token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        var userBO = UserControllerMapper.convertUpdateUserToBO(updateUser);
        var updateBO = this.userService.updateByAttribute(token, id, email, userBO);
        var response = RestResponseMapper.convertToSuccessResponse(updateBO);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("user/{user}/subscribe")
    public ResponseEntity<?> subscribeIndustry(HttpHeaders headers,
                                               UUID id,
                                               UUID industry,
                                               boolean control) {
        var token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        var bo = this.userService.subscribeIndustry(token,id,industry,control);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("user/{user}/unsubscribe")
    public ResponseEntity<?> unSubscribeIndustry(HttpHeaders headers, UUID id, UUID industry, boolean control) {
        var token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        var bo = this.userService.unSubscribeIndustry(token,id,industry,control);
        var response = RestResponseMapper.convertToSuccessResponse(bo);
        return ResponseEntity.ok(response);
    }


}
