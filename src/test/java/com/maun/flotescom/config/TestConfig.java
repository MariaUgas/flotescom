package com.maun.flotescom.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WebTestClient webTestClient() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:${local.server.port}")
                .responseTimeout(Duration.ofSeconds(10))
                .build();
    }
}