package com.ecommers.wishlist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wishlistOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommers - Wishlist API")
                        .description("Microservicio Wishlist del sistema e-commerce")
                        .version("1.0.0")
                        .contact(new Contact().name("dunedains")));
    }
}
