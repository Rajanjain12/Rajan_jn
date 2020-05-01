package com.kyobeeUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.braintreegateway.BraintreeGateway;
import com.kyobeeUserService.util.BrainTreeUtil;
import com.kyobeeUserService.util.LoggerUtil;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class KyobeeUserServiceApplication {

	@Autowired
	private BrainTreeUtil brainTreeUtil;

	public static void main(String[] args) {
		SpringApplication.run(KyobeeUserServiceApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public BraintreeGateway getBraintreeGateway() {
		BraintreeGateway gateway = brainTreeUtil.getBrainTreeGateway();
		LoggerUtil.logInfo("gateway:" + gateway);

		return gateway;
	}
}
