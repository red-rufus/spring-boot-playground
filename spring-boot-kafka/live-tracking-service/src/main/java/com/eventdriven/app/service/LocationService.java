package com.eventdriven.app.service;

import com.eventdriven.app.AppConstants;
import com.eventdriven.app.dto.DriverStatus;
import com.eventdriven.app.dto.LocationTelemetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    @KafkaListener(topics = AppConstants.LOCATION_TOPIC, groupId = "location-group", containerFactory = "locationKafkaListenerContainerFactory")
    public void processLocationUpdate(LocationTelemetry location) {
        log.info("Received location update: {}", location);
    }

    @KafkaListener(topics = AppConstants.CAR_DELIVERIES, groupId = "location-group", containerFactory = "locationKafkaListenerContainerFactory")
    public void processCarDelivery(LocationTelemetry location) {
        log.info("Received car delivery update: {}", location);
    }

    @KafkaListener(topics = AppConstants.BIKE_DELIVERIES, groupId = "location-group", containerFactory = "locationKafkaListenerContainerFactory")
    public void processBikeDelivery(LocationTelemetry location) {
        log.info("Received bike delivery update: {}", location);
    }

    @KafkaListener(topics = AppConstants.TRUCK_DELIVERIES, groupId = "location-group", containerFactory = "locationKafkaListenerContainerFactory")
    public void processTruckDelivery(LocationTelemetry location) {
        log.info("Received truck delivery update: {}", location);
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000), dltTopicSuffix = "-dlt")
    @KafkaListener(topics = AppConstants.DRIVER_STATUS_UPDATES, groupId = "driver-status-group", containerFactory = "driverStatusKafkaListenerContainerFactory")
    public void processDriverStatusUpdate(DriverStatus status) {
        if(status == null) {
            throw new RuntimeException("Null driver status received");
        }
        log.info("Received driver status update: {}", status);
    }

    @DltHandler
    public void listenDLT(DriverStatus status) {
        log.info("Received message in DLT: {}", status);
    }
}
