package com.maun.flotescom.service.impl;

import com.maun.flotescom.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

     public Mono<Void> sendNotification(UUID truckId, String message) {
        // Simula el envío de una notificación
        log.info("Sending notification: Truck ID: {}, Message: {}", truckId, message);
        return Mono.empty();
    }
}


