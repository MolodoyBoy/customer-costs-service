package com.oleg.customer.costs.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        Components components = new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme);

        SecurityRequirement requirement = new SecurityRequirement()
            .addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
            .components(components)
            .addSecurityItem(requirement);
    }
}