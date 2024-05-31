package com.sysmei.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("Sysmei - Sistema para Micro-Empreendedor Individual")
                        .description("API Sysmei")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Wesley Vicente")
                                .url("https://github.com/wesleyvicen")
                                .email("wesley1535@hotmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}
