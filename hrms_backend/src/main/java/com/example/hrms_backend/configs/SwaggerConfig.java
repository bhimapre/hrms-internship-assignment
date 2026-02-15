package com.example.hrms_backend.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI employeeOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("HRMS API")
                        .version("1.0")
                        .description("API documentation for managing employees, travel, expense, game booking, job opening"));

    }
}
