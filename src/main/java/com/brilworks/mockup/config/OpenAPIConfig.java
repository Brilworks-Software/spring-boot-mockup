package com.brilworks.mockup.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Mockup API")
                        .version("1.0.0")
                        .description("Brilwork Spring Boot Mockup API Documentation")
                        .contact(new Contact()
                                .name("Customer Support")
                                .email("hello@brilworks.com"))
                ).servers(List.of(
                        new Server()
                                .url("http://localhost:5000")
                ));
    }
}
