package com.ecommers.whitelist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI whitelistOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommers - Whitelist API")
                        .description("Microservicio Whitelist del sistema e-commerce")
                        .version("1.0.0")
                        .contact(new Contact().name("dunedains")));
    }
}
