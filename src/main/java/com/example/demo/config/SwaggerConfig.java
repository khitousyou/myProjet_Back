package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
@Configuration
public class SwaggerConfig {
	
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Documentation")
                        .description("API Documentation for Spring Boot project")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://localhost:8080")))
                .externalDocs(new ExternalDocumentation()
                        .description("Full Documentation")
                        .url("http://localhost:8080"));
    }
}
