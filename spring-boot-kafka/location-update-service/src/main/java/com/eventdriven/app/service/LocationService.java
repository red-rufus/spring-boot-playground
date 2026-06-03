package com.eventdriven.app.service;

import com.eventdriven.app.AppConstants;
import com.eventdriven.app.dto.LocationTelemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class LocationService {

    @Autowired
    private KafkaTemplate<String, LocationTelemetry> kafkaTemplate;

    public void updateLocation(LocationTelemetry location) {
        CompletableFuture<SendResult<String, LocationTelemetry>> future = kafkaTemplate.send(AppConstants.LOCATION_TOPIC, location.getDriverId(), location);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + location +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        location + "] due to : " + ex.getMessage());
            }
        });
    }
}
