package com.maun.flotescom.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NotificationService {
    Mono<Void> sendNotification(UUID truckId, String message);
}
