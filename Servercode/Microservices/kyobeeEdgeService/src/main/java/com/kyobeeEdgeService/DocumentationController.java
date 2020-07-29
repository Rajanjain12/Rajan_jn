package com.kyobeeEdgeService;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * This class provides common Swagger for all the micro-services.
 * @author Ria
 * 
 */
@Component
@Primary 
@EnableAutoConfiguration
//Override Original swagger implementation with custom swagger resource provider.
public class DocumentationController implements SwaggerResourcesProvider {

    @Override
    public List get() {
    	//Add each micro-service as swagger-resource.
        List resources = new ArrayList();
        resources.add(swaggerResource("user-service", "/kyobee-user-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("waitlist-service", "/kyobee-waitlist-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("account-service", "/kyobee-account-service/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}