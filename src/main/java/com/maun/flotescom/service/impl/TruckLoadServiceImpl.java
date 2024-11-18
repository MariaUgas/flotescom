package com.maun.flotescom.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

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

    // Operaciones CRUD para Truck
    public Mono<Truck> createTruck(TruckRequest request) {
        Truck truck = new Truck();
        truck.setLicensePlate(request.getLicensePlate());
        truck.setModel(request.getModel());
        truck.setCapacityLimit(request.getCapacityLimit());
        truck.setCurrentLoad(0.0);
        truck.setStatus(TruckStatus.AVAILABLE);
        
        return truckRepository.save(truck);
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
                truck.setLicensePlate(request.getLicensePlate());
                truck.setModel(request.getModel());
                truck.setCapacityLimit(request.getCapacityLimit());
                return truckRepository.save(truck);
            });
    }

    public Mono<Void> deleteTruck(UUID id) {
        return truckRepository.findById(id)
                .flatMap(truck ->
                        loadRepository.deleteByTruckId(id)
                                .then(truckRepository.deleteById(id))
                )
                .switchIfEmpty(Mono.empty());
    }

    // Operaciones de carga
    public Mono<Load> assignLoad(UUID truckId, LoadRequest request) {
        return truckRepository.findById(truckId)
            .switchIfEmpty(Mono.error(new RuntimeException("Truck not found")))
            .filter(truck -> truck.getStatus() == TruckStatus.AVAILABLE)
            .switchIfEmpty(Mono.error(new RuntimeException("Truck is not available")))
            .filter(truck -> truck.getCapacityLimit() >= request.getVolume())
            .switchIfEmpty(Mono.error(new RuntimeException("Load exceeds truck capacity")))
            .flatMap(truck -> {
                Load load = new Load();
                load.setTruckId(truckId);
                load.setVolume(request.getVolume());
                load.setDescription(request.getDescription());
                load.setLoadTimestamp(LocalDateTime.now());
                
                truck.setCurrentLoad(request.getVolume());
                truck.setStatus(TruckStatus.LOADED);
                
                return truckRepository.save(truck)
                    .then(loadRepository.save(load));
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
}
