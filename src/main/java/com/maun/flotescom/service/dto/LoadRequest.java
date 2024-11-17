package com.maun.flotescom.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información requerida para registrar la carga")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequest {
    
    private Double volume;
    

    private String description;
}