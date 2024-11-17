package com.maun.flotescom.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.maun.flotescom.enums.TruckStatus;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Entidad utilizada para registrar la información básica del camión")

@Table("trucks")
public class Truck {
    @Id
    private UUID id;
    
    @Column("license_plate")
    private String licensePlate;
    
    private String model;
    
    @Column("capacity_limit")
    private Double capacityLimit;
    
    @Column("current_load")
    private Double currentLoad;
    
    @Column("status")
    private TruckStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getCapacityLimit() {
        return capacityLimit;
    }

    public void setCapacityLimit(Double capacityLimit) {
        this.capacityLimit = capacityLimit;
    }

    public Double getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(Double currentLoad) {
        this.currentLoad = currentLoad;
    }

    public TruckStatus getStatus() {
        return status;
    }

    public void setStatus(TruckStatus status) {
        this.status = status;
    }
}