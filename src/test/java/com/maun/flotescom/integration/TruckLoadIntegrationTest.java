package com.maun.flotescom.integration;

import com.maun.flotescom.FlotescomApplication;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.enums.TruckStatus;
import com.maun.flotescom.repository.LoadRepository;
import com.maun.flotescom.repository.TruckRepository;
import com.maun.flotescom.service.dto.TruckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

@SpringBootTest(
        classes = FlotescomApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class FlotescomIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private LoadRepository loadRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void contextLoads() {
        // Verifica que el contexto carga correctamente
    }

    @Test
    void shouldCreateTruckAndVerifyInDatabase() {
        // Crear request con placa única
        String uniqueLicense = "INT-TEST-" + UUID.randomUUID().toString().substring(0, 8);
        TruckRequest request = new TruckRequest();
        request.setLicensePlate(uniqueLicense);
        request.setModel("Integration Test Model");
        request.setCapacityLimit(1000.0);

        // Crear camión a través del API
        Mono<Truck> createdTruckMono = webTestClient
                .post()
                .uri("/trucks")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Truck.class)
                .getResponseBody()
                .next();

        StepVerifier.create(createdTruckMono)
                .expectNextMatches(truck ->
                        truck.getLicensePlate().equals(uniqueLicense) &&
                                truck.getModel().equals("Integration Test Model") &&
                                truck.getCapacityLimit().equals(1000.0) &&
                                truck.getCurrentLoad().equals(0.0) &&
                                truck.getStatus().equals(TruckStatus.AVAILABLE)
                )
                .verifyComplete();
    }
    @Test
    void shouldRetrieveTruckFromDatabase() {
        String uniqueLicense = "INT-TEST-" + UUID.randomUUID().toString().substring(0, 8);
        Truck truck = new Truck();
        truck.setLicensePlate(uniqueLicense);
        truck.setModel("Direct DB Model");
        truck.setCapacityLimit(2000.0);
        truck.setCurrentLoad(0.0);
        truck.setStatus(TruckStatus.AVAILABLE);

        webTestClient
                .post()
                .uri("/trucks")
                .bodyValue(truck)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Truck.class)
                .value(savedTruck -> {
                    webTestClient
                            .get()
                            .uri("/trucks/{id}", savedTruck.getId())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(Truck.class)
                            .value(retrievedTruck -> {
                                assert uniqueLicense.equals(retrievedTruck.getLicensePlate());
                                assert "Direct DB Model".equals(retrievedTruck.getModel());
                                assert Double.valueOf(2000.0).equals(retrievedTruck.getCapacityLimit());
                            });
                });
    }


}