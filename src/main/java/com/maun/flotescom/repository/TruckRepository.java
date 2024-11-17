package com.maun.flotescom.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.enums.TruckStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Schema(description = "Registro de información del camión")
@Repository
public interface TruckRepository extends R2dbcRepository<Truck, UUID> {
	  Mono<Truck> findByLicensePlate(String licensePlate);
	    
	    @Query("SELECT * FROM trucks WHERE status = :status")
	    Flux<Truck> findByStatus(TruckStatus status);
	    
	    @Query("SELECT * FROM trucks WHERE status = :status AND (capacity_limit - current_load) >= :requiredVolume")
	    Flux<Truck> findAvailableTrucksForVolume(TruckStatus status, Double requiredVolume);
}
