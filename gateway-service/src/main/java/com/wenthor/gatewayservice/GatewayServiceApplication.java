package com.wenthor.gatewayservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}
