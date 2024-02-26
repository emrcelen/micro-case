package com.wenthor;

import com.wenthor.response.IndustryResponse;
import com.wenthor.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "industry-service")
public interface IndustryServiceClient {
    @GetMapping(path = "/industry/{id}")
    ResponseEntity<RestResponse<IndustryResponse>> findByID(@PathVariable(name = "id") UUID id);

    @PostMapping(path = "/industry/{industry}/subscribe")
    ResponseEntity<RestResponse<IndustryResponse>> subsribeIndustry(@RequestHeader HttpHeaders headers,
                                                                    @PathVariable(name = "industry") UUID industryID,
                                                                    @RequestParam(name = "user") UUID userID);
    @PostMapping(path = "/industry/{industry}/unsubscribe")
    ResponseEntity<RestResponse<IndustryResponse>> unSubsribeIndustry(@RequestHeader HttpHeaders headers,
                                                                      @PathVariable(name = "industry") UUID industryID,
                                                                      @RequestParam(name = "user") UUID userID);
}
