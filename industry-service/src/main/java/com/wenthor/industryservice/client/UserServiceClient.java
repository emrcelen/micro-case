package com.wenthor.industryservice.client;

import com.wenthor.industryservice.controller.dto.response.rest.RestResponse;
import com.wenthor.industryservice.client.response.user.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/find")
    ResponseEntity<RestResponse<ResponseUser>> findByAttribute(@RequestParam(name = "id") UUID id,
                                                               @RequestParam(name = "email") String email);
    @PostMapping("/user/{user}/subscribe")
    ResponseEntity<RestResponse<ResponseUser>> subscribeIndustry(@RequestHeader
                                                                 HttpHeaders headers,
                                                                 @PathVariable(name = "user") UUID id,
                                                                 @RequestParam(name = "industry") UUID industry,
                                                                 @RequestParam(name = "client", required = false) boolean control);
    @PostMapping("/user/{user}/unsubscribe")
    ResponseEntity<RestResponse<ResponseUser>> unSubscribeIndustry(@RequestHeader
                                                                   HttpHeaders headers,
                                                                   @PathVariable(name = "user") UUID id,
                                                                   @RequestParam(name = "industry") UUID industry,
                                                                   @RequestParam(name = "client", required = false) boolean control);
}
