package com.eventdriven.app.controller;

import com.eventdriven.app.dto.LocationTelemetry;
import com.eventdriven.app.service.LocationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/location")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Helper endpoint to generate and publish N random locations for testing.
     */
    @PostConstruct
    public void update() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            LocationTelemetry location = LocationTelemetry.builder()
                    .driverId("D"+ThreadLocalRandom.current().nextInt(10))
                    .latitude(ThreadLocalRandom.current().nextDouble(-90.0, 90.0))
                    .longitude(ThreadLocalRandom.current().nextDouble(-180.0, 180.0))
                    .timestamp(System.currentTimeMillis())
                    .speed(ThreadLocalRandom.current().nextDouble(0, 130))
                    .distance(ThreadLocalRandom.current().nextDouble(10))
                    .city(List.of("New Delhi", "Mumbai", "Kolkata", "Hyderabad", "Bengaluru", "Pune").get(ThreadLocalRandom.current().nextInt(6)))
                    .vehicleType(List.of("BIKE", "CAR", "TRUCK").get(ThreadLocalRandom.current().nextInt(3)))
                    .driverId("driver" + ThreadLocalRandom.current().nextInt(1, 100))
                    .deliveryCompleted(ThreadLocalRandom.current().nextBoolean())
                    .build();
            locationService.updateLocation(location);
            Thread.sleep(500);
        }
        log.info("Generated and published 50 location(s)");
    }

}
