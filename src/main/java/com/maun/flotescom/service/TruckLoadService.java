package com.maun.flotescom.service;

import java.util.UUID;

import com.maun.flotescom.entity.Load;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.service.dto.LoadRequest;
import com.maun.flotescom.service.dto.TruckRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TruckLoadService {
	Mono<Truck> createTruck(TruckRequest request);
	Mono<Truck> getTruck(UUID id);
	Flux<Truck> getAllTrucks();
	Mono<Truck> updateTruck(UUID id, TruckRequest request);
	Mono<Void> deleteTruck(UUID id);
	Mono<Load> assignLoad(UUID truckId, LoadRequest request);
	Mono<Load> unloadTruck(UUID truckId);
}
