package com.kyobeeDiscoveryService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class KyobeeDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KyobeeDiscoveryServiceApplication.class, args);
	}

}
