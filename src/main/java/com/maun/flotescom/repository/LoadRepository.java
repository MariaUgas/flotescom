package com.maun.flotescom.repository;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.maun.flotescom.entity.Load;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Schema(description = "Registro del estado y cantidad de carga del camión")
@Repository
public interface LoadRepository extends ReactiveCrudRepository<Load, UUID> {
	Flux<Load> findByTruckId(UUID truckId);
    Mono<Load> findByTruckIdAndUnloadTimestampIsNull(UUID truckId);
}
