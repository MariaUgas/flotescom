package com.maun.flotescom.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import com.maun.flotescom.service.NotificationService;
import com.maun.flotescom.service.TruckLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maun.flotescom.entity.Load;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.enums.TruckStatus;
import com.maun.flotescom.repository.LoadRepository;
import com.maun.flotescom.repository.TruckRepository;
import com.maun.flotescom.service.dto.LoadRequest;
import com.maun.flotescom.service.dto.TruckRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TruckLoadServiceImpl implements TruckLoadService {
	@Autowired
    private TruckRepository truckRepository;
	@Autowired
    private LoadRepository loadRepository;
    @Autowired
    private NotificationService notificationService;

    // Operaciones CRUD para Truck
    public Mono<Truck> createTruck(TruckRequest request) {
        Truck truck = new Truck();
        truck.setLicensePlate(request.getLicensePlate());
        truck.setModel(request.getModel());
        truck.setCapacityLimit(request.getCapacityLimit());
        truck.setCurrentLoad(0.0);
        truck.setStatus(TruckStatus.AVAILABLE);

        return truckRepository.save(truck)
                .doOnSuccess(createdTruck -> log.info("Truck {} created successfully.", createdTruck.getId()));
    }

    public Mono<Truck> getTruck(UUID id) {
        return truckRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Truck not found")));
    }

    public Flux<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    public Mono<Truck> updateTruck(UUID id, TruckRequest request) {
        return truckRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Truck not found")))
            .flatMap(truck -> {
                validateTruckStatus(truck, TruckStatus.AVAILABLE);
                truck.setLicensePlate(request.getLicensePlate());
                truck.setModel(request.getModel());
                truck.setCapacityLimit(request.getCapacityLimit());
                return truckRepository.save(truck);
            })
            .doOnSuccess(updatedTruck -> log.info("Truck {} updated successfully.", updatedTruck.getId()));

    }

    public Mono<Void> deleteTruck(UUID id) {
        return truckRepository.findById(id)
            .flatMap(truck -> {
                validateTruckStatus(truck, TruckStatus.AVAILABLE);
                return loadRepository.deleteByTruckId(id)
                        .then(truckRepository.deleteById(id));
            })
            .switchIfEmpty(Mono.empty())
            .doOnSuccess(unused -> log.info("Truck {} deleted successfully.", id));
    }

    // Operaciones de carga
    public Mono<Load> assignLoad(UUID truckId, LoadRequest request) {
        return truckRepository.findById(truckId)
                .switchIfEmpty(Mono.error(new RuntimeException("Truck not found")))
                .flatMap(truck -> {
                    validateTruckStatus(truck, TruckStatus.AVAILABLE);
                    if (truck.getCapacityLimit() < request.getVolume()) {
                        throw new RuntimeException("Load exceeds truck capacity");
                    }

                    Load load = new Load();
                    load.setTruckId(truckId);
                    load.setVolume(request.getVolume());
                    load.setDescription(request.getDescription());
                    load.setLoadTimestamp(LocalDateTime.now());

                    truck.setCurrentLoad(request.getVolume());
                    truck.setStatus(TruckStatus.LOADED);

                    return truckRepository.save(truck)
                            .then(loadRepository.save(load));
                })
                .doOnSuccess(load -> {
                    log.info("Load assigned to truck {} successfully.", truckId);
                    notificationService.sendNotification(truckId, "Load assigned");  // Notificación
                });

    }

    public Mono<Load> unloadTruck(UUID truckId) {
        return loadRepository.findByTruckIdAndUnloadTimestampIsNull(truckId)
            .switchIfEmpty(Mono.error(new RuntimeException("No active load found")))
            .flatMap(load -> {
                load.setUnloadTimestamp(LocalDateTime.now());
                
                return truckRepository.findById(truckId)
                    .flatMap(truck -> {
                        truck.setCurrentLoad(0.0);
                        truck.setStatus(TruckStatus.UNLOADED);
                        return truckRepository.save(truck)
                            .then(loadRepository.save(load));
                    });
            });
    }
    private void validateTruckStatus(Truck truck, TruckStatus expectedStatus) {
        if (truck.getStatus() != expectedStatus) {
            throw new RuntimeException("Truck is not in the expected state: " + expectedStatus);
        }
    }

}
