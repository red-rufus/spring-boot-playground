package com.eventdriven.app.config;

import com.eventdriven.app.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class LocationConfig {

    @Bean
    public NewTopic locationTopic() {
        return TopicBuilder.name(AppConstants.LOCATION_TOPIC)
                .build();
    }

    @Bean
    public NewTopic carDeliveries() {
        return TopicBuilder.name(AppConstants.CAR_DELIVERIES)
                .build();
    }

    @Bean
    public NewTopic bikeDeliveries() {
        return TopicBuilder.name(AppConstants.BIKE_DELIVERIES)
                .build();
    }

    @Bean
    public NewTopic truckDeliveries() {
        return TopicBuilder.name(AppConstants.TRUCK_DELIVERIES)
                .build();
    }

}
