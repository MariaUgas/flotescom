package com.maun.flotescom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("Flotescom")
                .description("API para manejar camiones y sus carga")
                .version("1.0")
                .contact(new Contact()
                    .name("Maria Ugas")
                    .email("marria42@gmail.com")
                    .url("www.linkedin.com/in/maría-de-los-angeles-ugas-navarro-4143a534"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
