package com.maun.flotescom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.maun.flotescom.config.OpenApiConfig;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@Import(OpenApiConfig.class)  // Importa la configuración de OpenAPI
public class FlotescomApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlotescomApplication.class, args);
	}

}
