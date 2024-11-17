package com.maun.flotescom.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(description = "Información requerida de un camión")
@Data
@NoArgsConstructor
public class TruckRequest {
   
    private String licensePlate;
    
    
    private String model;
    
   
    private Double capacityLimit;
}
