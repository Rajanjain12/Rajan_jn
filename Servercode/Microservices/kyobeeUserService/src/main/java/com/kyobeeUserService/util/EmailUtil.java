package com.kyobeeUserService.util;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kyobeeUserService.dto.ResponseDTO;

@Component
public class EmailUtil {

	@Autowired
	private WebClient.Builder webClientBuilder;

	public void sendEmail(String toEmail, String subject, String body) {
		webClientBuilder.baseUrl(UserServiceConstants.UTIL_BASE_URL);
		webClientBuilder.build().get()
				.uri(uriBuilder -> uriBuilder.path(UserServiceConstants.EMAIL_PATH)
						.queryParam(UserServiceConstants.TO_EMAIL, toEmail)
						.queryParam(UserServiceConstants.SUBJECT, subject).queryParam(UserServiceConstants.BODY, body)
						.build())
				.header(UserServiceConstants.CONTENT_TYPE, UserServiceConstants.CONTENT_TYPE_VALUE)
				.header(UserServiceConstants.ACCEPT, UserServiceConstants.API_VERSION).retrieve()
				.bodyToMono(ResponseDTO.class).block();
		LoggerUtil.logInfo("Entering Util service");

	}

	public VelocityEngine velocityTemplate() {

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty(UserServiceConstants.VELOCITY_PROPERTY, ClasspathResourceLoader.class.getName());
		ve.init();
		return ve;
	}

}
