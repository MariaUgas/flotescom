package com.maun.flotescom.controller;

import com.maun.flotescom.entity.Load;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.enums.TruckStatus;
import com.maun.flotescom.service.TruckLoadService;
import com.maun.flotescom.service.dto.LoadRequest;
import com.maun.flotescom.service.dto.TruckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TruckLoadControllerTest {

    @Mock
    private TruckLoadService truckLoadService;

    @InjectMocks
    private TruckLoadController truckLoadController;

    private WebTestClient webTestClient;
    private UUID testId;
    private Truck testTruck;
    private TruckRequest testTruckRequest;
    private Load testLoad;
    private LoadRequest testLoadRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(truckLoadController).build();

        testId = UUID.randomUUID();

        // Initialize test truck
        testTruck = new Truck();
        testTruck.setId(testId);
        testTruck.setLicensePlate("TEST123");
        testTruck.setModel("TestModel");
        testTruck.setCapacityLimit(1000.0);
        testTruck.setCurrentLoad(0.0);
        testTruck.setStatus(TruckStatus.AVAILABLE);

        // Initialize test truck request
        testTruckRequest = new TruckRequest();
        testTruckRequest.setLicensePlate("TEST123");
        testTruckRequest.setModel("TestModel");
        testTruckRequest.setCapacityLimit(1000.0);

        // Initialize test load
        testLoad = new Load();
        testLoad.setId(UUID.randomUUID());
        testLoad.setTruckId(testId);
        testLoad.setVolume(500.0);
        testLoad.setDescription("Test Load");
        testLoad.setLoadTimestamp(LocalDateTime.now());

        // Initialize test load request
        testLoadRequest = new LoadRequest();
        testLoadRequest.setVolume(500.0);
        testLoadRequest.setDescription("Test Load");
    }

    @Test
    void createTruck_Success() {
        when(truckLoadService.createTruck(any(TruckRequest.class)))
                .thenReturn(Mono.just(testTruck));

        Mono<Truck> responseMono = webTestClient.post()
                .uri("/trucks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTruckRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Truck.class)
                .getResponseBody()
                .next();

        StepVerifier.create(responseMono)
                .expectNextMatches(truck ->
                        truck.getId().equals(testTruck.getId()) &&
                                truck.getLicensePlate().equals(testTruck.getLicensePlate()) &&
                                truck.getModel().equals(testTruck.getModel()) &&
                                truck.getCapacityLimit().equals(testTruck.getCapacityLimit())
                )
                .verifyComplete();
    }

    @Test
    void getTruck_Success() {
        when(truckLoadService.getTruck(testId))
                .thenReturn(Mono.just(testTruck));

        Mono<Truck> responseMono = webTestClient.get()
                .uri("/trucks/" + testId)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Truck.class)
                .getResponseBody()
                .next();

        StepVerifier.create(responseMono)
                .expectNextMatches(truck ->
                        truck.getId().equals(testTruck.getId()) &&
                                truck.getLicensePlate().equals(testTruck.getLicensePlate())
                )
                .verifyComplete();
    }

    @Test
    void getTruck_NotFound() {
        when(truckLoadService.getTruck(testId))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/trucks/" + testId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllTrucks_Success() {
        when(truckLoadService.getAllTrucks())
                .thenReturn(Flux.just(testTruck));

        Flux<Truck> responseFlux = webTestClient.get()
                .uri("/trucks")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Truck.class)
                .getResponseBody();

        StepVerifier.create(responseFlux)
                .expectNextMatches(truck ->
                        truck.getId().equals(testTruck.getId()) &&
                                truck.getLicensePlate().equals(testTruck.getLicensePlate()) &&
                                truck.getModel().equals(testTruck.getModel()) &&
                                truck.getCapacityLimit().equals(testTruck.getCapacityLimit()) &&
                                truck.getCurrentLoad().equals(testTruck.getCurrentLoad()) &&
                                truck.getStatus().equals(testTruck.getStatus())
                )
                .verifyComplete();
    }


    @Test
    void updateTruck_Success() {
        when(truckLoadService.updateTruck(eq(testId), any(TruckRequest.class)))
                .thenReturn(Mono.just(testTruck));
        Mono<Truck> responseMono = webTestClient.put()
                .uri("/trucks/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTruckRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Truck.class)
                .getResponseBody()
                .next();
        StepVerifier.create(responseMono)
                .expectNextMatches(truck ->
                        truck.getId().equals(testTruck.getId()) &&
                                truck.getLicensePlate().equals(testTruck.getLicensePlate()) &&
                                truck.getModel().equals(testTruck.getModel()) &&
                                truck.getCapacityLimit().equals(testTruck.getCapacityLimit()) &&
                                truck.getCurrentLoad().equals(testTruck.getCurrentLoad()) &&
                                truck.getStatus().equals(testTruck.getStatus())
                )
                .verifyComplete();
    }

    @Test
    void updateTruck_NotFound() {
        when(truckLoadService.updateTruck(eq(testId), any(TruckRequest.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/trucks/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testTruckRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteTruck_Success() {
        when(truckLoadService.deleteTruck(testId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/trucks/" + testId)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void assignLoad_Success() {
        when(truckLoadService.assignLoad(eq(testId), any(LoadRequest.class)))
                .thenReturn(Mono.just(testLoad));
        Mono<Load> responseMono = webTestClient.post()
                .uri("/trucks/" + testId + "/loads")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testLoadRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Load.class)
                .getResponseBody()
                .next();
        StepVerifier.create(responseMono)
                .expectNextMatches(load ->
                        load.getTruckId().equals(testLoad.getTruckId()) &&
                                load.getVolume().equals(testLoad.getVolume()) &&
                                load.getDescription().equals(testLoad.getDescription()) &&
                                load.getLoadTimestamp() != null
                )
                .verifyComplete();
    }

    @Test
    void assignLoad_BadRequest() {
        when(truckLoadService.assignLoad(eq(testId), any(LoadRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("Load exceeds capacity")));

        webTestClient.post()
                .uri("/trucks/" + testId + "/loads")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testLoadRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}