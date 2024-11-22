package com.maun.flotescom.service.impl;

import com.maun.flotescom.entity.Load;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.enums.TruckStatus;
import com.maun.flotescom.repository.LoadRepository;
import com.maun.flotescom.repository.TruckRepository;
import com.maun.flotescom.service.dto.LoadRequest;
import com.maun.flotescom.service.dto.TruckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TruckLoadServiceImplTest {

    @Mock
    private TruckRepository truckRepository;

    @Mock
    private LoadRepository loadRepository;

    @Mock private NotificationServiceImpl notificationService;

    @InjectMocks
    private TruckLoadServiceImpl truckLoadService;

    private UUID testId;
    private Truck testTruck;
    private TruckRequest testTruckRequest;
    private Load testLoad;
    private LoadRequest testLoadRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(testTruck));

        StepVerifier.create(truckLoadService.createTruck(testTruckRequest))
                .expectNext(testTruck)
                .verifyComplete();
    }

    @Test
    void getTruck_Success() {
        when(truckRepository.findById(testId)).thenReturn(Mono.just(testTruck));

        StepVerifier.create(truckLoadService.getTruck(testId))
                .expectNext(testTruck)
                .verifyComplete();
    }

    @Test
    void getTruck_NotFound() {
        when(truckRepository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.getTruck(testId))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getAllTrucks_Success() {
        when(truckRepository.findAll()).thenReturn(Flux.just(testTruck));

        StepVerifier.create(truckLoadService.getAllTrucks())
                .expectNext(testTruck)
                .verifyComplete();
    }

    @Test
    void updateTruck_Success() {
        when(truckRepository.findById(testId)).thenReturn(Mono.just(testTruck));
        when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(testTruck));

        StepVerifier.create(truckLoadService.updateTruck(testId, testTruckRequest))
                .expectNext(testTruck)
                .verifyComplete();
    }

    @Test
    void updateTruck_NotFound() {
        when(truckRepository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.updateTruck(testId, testTruckRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void deleteTruck_Success() {
        Truck localTruck=new Truck();
        localTruck.setId(testId);
        localTruck.setStatus(TruckStatus.AVAILABLE);
        // Mock findById
        when(truckRepository.findById(testId))
                .thenReturn(Mono.just(localTruck));

        // Mock loadRepository deletion
        when(loadRepository.deleteByTruckId(testId))
                .thenReturn(Mono.empty());

        // Mock truck deletion
        when(truckRepository.deleteById(testId))
                .thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.deleteTruck(testId))
                .verifyComplete();

        verify(loadRepository).deleteByTruckId(testId);
        verify(truckRepository).deleteById(testId);
    }

    @Test
    void assignLoad_Success() {
        when(truckRepository.findById(testId)).thenReturn(Mono.just(testTruck));
        when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(testTruck));
        when(loadRepository.save(any(Load.class))).thenReturn(Mono.just(testLoad));
        when(notificationService.sendNotification(any(UUID.class), anyString())).thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.assignLoad(testId, testLoadRequest))
                .expectNext(testLoad)
                .verifyComplete();
    }

    @Test
    void assignLoad_TruckNotFound() {
        when(truckRepository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.assignLoad(testId, testLoadRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void assignLoad_ExceedsCapacity() {
        testLoadRequest.setVolume(2000.0); // Exceeds capacity
        when(truckRepository.findById(testId)).thenReturn(Mono.just(testTruck));

        StepVerifier.create(truckLoadService.assignLoad(testId, testLoadRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void unloadTruck_Success() {
        Load activeLoad = new Load();
        activeLoad.setTruckId(testId);
        activeLoad.setVolume(500.0);
        activeLoad.setLoadTimestamp(LocalDateTime.now());

        when(loadRepository.findByTruckIdAndUnloadTimestampIsNull(testId))
                .thenReturn(Mono.just(activeLoad));
        when(truckRepository.findById(testId)).thenReturn(Mono.just(testTruck));
        when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(testTruck));
        when(loadRepository.save(any(Load.class))).thenReturn(Mono.just(activeLoad));

        StepVerifier.create(truckLoadService.unloadTruck(testId))
                .expectNext(activeLoad)
                .verifyComplete();
    }

    @Test
    void unloadTruck_NoActiveLoad() {
        when(loadRepository.findByTruckIdAndUnloadTimestampIsNull(testId))
                .thenReturn(Mono.empty());

        StepVerifier.create(truckLoadService.unloadTruck(testId))
                .expectError(RuntimeException.class)
                .verify();
    }
}