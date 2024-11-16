package com.maun.flotescom.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoadRequest {
    
    private Double volume;
    

    private String description;
}