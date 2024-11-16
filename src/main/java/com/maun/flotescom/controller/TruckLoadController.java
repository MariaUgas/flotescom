package com.maun.flotescom.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maun.flotescom.entity.Load;
import com.maun.flotescom.entity.Truck;
import com.maun.flotescom.service.dto.LoadRequest;
import com.maun.flotescom.service.dto.TruckRequest;
import com.maun.flotescom.service.impl.TruckLoadServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/trucks")
public class TruckLoadController {
	
	@Autowired
	private  TruckLoadServiceImpl truckLoadService;

    @PostMapping
    public Mono<Truck> createTruck(@RequestBody TruckRequest request) {
        return truckLoadService.createTruck(request);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Truck>> getTruck(@PathVariable UUID id) {
        return truckLoadService.getTruck(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Truck> getAllTrucks() {
        return truckLoadService.getAllTrucks();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Truck>> updateTruck(
            @PathVariable UUID id,
            @RequestBody TruckRequest request) {
        return truckLoadService.updateTruck(id, request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTruck(@PathVariable UUID id) {
        return truckLoadService.deleteTruck(id);
    }

    @PostMapping("/{truckId}/loads")
    public Mono<ResponseEntity<Load>> assignLoad(
            @PathVariable UUID truckId,
            @RequestBody LoadRequest request) {
        return truckLoadService.assignLoad(truckId, request)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/{truckId}/unload")
    public Mono<ResponseEntity<Load>> unloadTruck(@PathVariable UUID truckId) {
        return truckLoadService.unloadTruck(truckId)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
