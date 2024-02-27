package com.wenthor.invitationservice.client;


import com.wenthor.invitationservice.client.response.user.ResponseUser;
import com.wenthor.invitationservice.controller.dto.response.rest.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/find")
    ResponseEntity<RestResponse<ResponseUser>> findByAttribute(@RequestParam(name = "id") UUID id,
                                                               @RequestParam(name = "email") String email);
}
