package com.payflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI payflowOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("PayFlow Banking API")
                        .description(
                                "REST API for a digital wallet and banking system. " +
                                        "Supports user registration, account management, " +
                                        "deposits, withdrawals, and fund transfers."
                        )
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Priyank Sharma")
                                .url("https://github.com/PRIYANK097")

                        )
                );
    }
}