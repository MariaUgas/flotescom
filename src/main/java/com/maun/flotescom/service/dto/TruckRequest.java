package com.maun.flotescom.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruckRequest {
   
    private String licensePlate;
    
    
    private String model;
    
   
    private Double capacityLimit;
}
