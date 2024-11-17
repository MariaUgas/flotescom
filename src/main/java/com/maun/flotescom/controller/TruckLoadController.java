package com.maun.flotescom.controller;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/trucks")
@Tag(name = "Flotescom", description = "APIs para manejo de camiones y sus cargas")
public class TruckLoadController {
	
	@Autowired
	private  TruckLoadServiceImpl truckLoadService;

    @Operation(summary = "Agregando un nuevo camión",
            description = "Ingrese la informacion del nuevo camión")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro creado con éxito",
                content = @Content(schema = @Schema(implementation = Truck.class))),
        @ApiResponse(responseCode = "400", description = "Los datos ingresados no son válidos")
    })
	@PostMapping
    public Mono<Truck> createTruck(@RequestBody TruckRequest request) {
        return truckLoadService.createTruck(request);
    }

    @Operation(summary = "Obtener camión por identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Camión encontrado",
                    content = @Content(schema = @Schema(implementation = Truck.class))),
            @ApiResponse(responseCode = "404", description = "Camión no encontrado")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Truck>> getTruck(@PathVariable UUID id) {
        return truckLoadService.getTruck(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener listado de camiones, lista completa")
    @ApiResponse(responseCode = "200", description = "Lista de todos los camiones existentes",
            content = @Content(schema = @Schema(implementation = Truck.class)))
    @GetMapping
    public Flux<Truck> getAllTrucks() {
        return truckLoadService.getAllTrucks();
    }

    @Operation(summary = "Actualizar información de un camión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del camión actualizados",
                    content = @Content(schema = @Schema(implementation = Truck.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el camión"),
            @ApiResponse(responseCode = "400", description = "Información ingresada inválida")
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Truck>> updateTruck(
            @PathVariable UUID id,
            @RequestBody TruckRequest request) {
        return truckLoadService.updateTruck(id, request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminación fisica del registro de un camión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro del camión eliminado"),
            @ApiResponse(responseCode = "404", description = "Camión no encontrado")
    })
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTruck(@PathVariable UUID id) {
        return truckLoadService.deleteTruck(id);
    }

    @Operation(summary = "Asignación de carga a un camión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Camión cargado existosamente",
                    content = @Content(schema = @Schema(implementation = Load.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, la carga excede la capacidad"),
            @ApiResponse(responseCode = "404", description = "Camión no encontrado")
    })
    @PostMapping("/{truckId}/loads")
    public Mono<ResponseEntity<Load>> assignLoad(
            @PathVariable UUID truckId,
            @RequestBody LoadRequest request) {
        return truckLoadService.assignLoad(truckId, request)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @Operation(summary = "Descarga de un camión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Camión descargado exitosamente",
                    content = @Content(schema = @Schema(implementation = Load.class))),
            @ApiResponse(responseCode = "400", description = "Operación inválida, el camión esta vacío"),
            @ApiResponse(responseCode = "404", description = "Camión no encontrado")
    })
    @PostMapping("/{truckId}/unload")
    public Mono<ResponseEntity<Load>> unloadTruck(@PathVariable UUID truckId) {
        return truckLoadService.unloadTruck(truckId)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
