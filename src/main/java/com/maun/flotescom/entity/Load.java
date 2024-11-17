package com.maun.flotescom.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entidad usada para registrar la carga")
@Table("loads")
public class Load {
    @Id
    private UUID id;
    
    @Column("truck_id")
    private UUID truckId;
    
    private Double volume;
    
    private String description;
    
    @Column("load_timestamp")
    private LocalDateTime loadTimestamp;
    
    @Column("unload_timestamp")
    private LocalDateTime unloadTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTruckId() {
        return truckId;
    }

    public void setTruckId(UUID truckId) {
        this.truckId = truckId;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLoadTimestamp() {
        return loadTimestamp;
    }

    public void setLoadTimestamp(LocalDateTime loadTimestamp) {
        this.loadTimestamp = loadTimestamp;
    }

    public LocalDateTime getUnloadTimestamp() {
        return unloadTimestamp;
    }

    public void setUnloadTimestamp(LocalDateTime unloadTimestamp) {
        this.unloadTimestamp = unloadTimestamp;
    }
}