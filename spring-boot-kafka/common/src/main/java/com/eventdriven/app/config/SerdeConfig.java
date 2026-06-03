package com.eventdriven.app.config;

import com.eventdriven.app.dto.DriverStatus;
import com.eventdriven.app.dto.LocationTelemetry;
import com.eventdriven.app.serde.DriverStatusSerde;
import com.eventdriven.app.serde.LocationSerde;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SerdeConfig {

    @Bean
    public Serde<LocationTelemetry> locationSerde() {
        Serde<LocationTelemetry> locationSerde = new LocationSerde();
        locationSerde.configure(Collections.singletonMap("spring.json.trusted.packages", "*"), false);
        return locationSerde;
    }

    @Bean
    public Serde<DriverStatus> driverStatusSerde() {
        Serde<DriverStatus> driverStatusSerde = new DriverStatusSerde();
        driverStatusSerde.configure(Collections.singletonMap("spring.json.trusted.packages", "*"), false);
        return driverStatusSerde;
    }

}
