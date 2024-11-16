package com.maun.flotescom.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.maun.flotescom.entity.Load;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoadRepository extends ReactiveCrudRepository<Load, UUID> {
	Flux<Load> findByTruckId(UUID truckId);
    Mono<Load> findByTruckIdAndUnloadTimestampIsNull(UUID truckId);
}
