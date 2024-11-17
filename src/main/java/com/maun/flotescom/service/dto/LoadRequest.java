package com.maun.flotescom.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Schema(description = "Información requerida para registrar la carga")
@Data
@Builder
public class LoadRequest {
    
    private Double volume;
    

    private String description;
}