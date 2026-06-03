package com.eventdriven.app.streams;

import com.eventdriven.app.AppConstants;
import com.eventdriven.app.dto.DriverStatus;
import com.eventdriven.app.dto.LocationTelemetry;
import com.eventdriven.app.serde.DriverStatusSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class DeliveryTopology {

    private static Logger log = LoggerFactory.getLogger(DeliveryTopology.class);

    @Autowired
    Serde<LocationTelemetry> locationSerde;

    @Autowired
    Serde<DriverStatus> driverStatusSerde;

    @Bean
    public KStream<String, LocationTelemetry> deliveryTrackingTopology(StreamsBuilder streamsBuilder) {

        KStream<String, LocationTelemetry> deliveries = streamsBuilder
                .stream(AppConstants.LOCATION_TOPIC, Consumed.with(Serdes.String(), locationSerde))
                .filter((key, location) -> Boolean.FALSE.equals(location.getDeliveryCompleted())); // safer null check

        //  1. City Order Count
        processCityStats(deliveries);

        //  2. Total Distance per Driver
        processDriverDistance(deliveries);

        //  3. Overspeeding Count (Windowed)
        processOverspeeding(deliveries);

        //  4. Split by Vehicle Type
        routeByVehicleType(deliveries);

        //  5. MapValues to DriverStatus
        publishDriverStatus(deliveries);

        // Return one of the main streams (or create a new merged one if needed)
        return deliveries;   // or return driverStatusStream if that's the main output
    }

    private void processCityStats(KStream<String, LocationTelemetry> deliveries) {
        deliveries
                .groupBy((key, location) -> location.getCity())
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("city-order-count")
                        .withCachingDisabled());
    }

    private void processDriverDistance(KStream<String, LocationTelemetry> deliveries) {
        deliveries
                .groupBy((key, location) -> location.getDriverId())
                .aggregate(
                        () -> 0.0,
                        (driverId, location, totalDistance) -> totalDistance +
                                (location.getDistance() != null ? location.getDistance() : 0.0),  // assuming you added this field
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("total-distance-per-driver")
                                .withValueSerde(Serdes.Double())
                );
    }

    private void processOverspeeding(KStream<String, LocationTelemetry> deliveries) {
        //Prefer .groupByKey() when the stream is already keyed by driverId. Re-keying with .groupBy(...) causes repartitioning (more expensive).
        deliveries
                .filter((key, location) -> location.getSpeed() != null && location.getSpeed() > 80)
                .groupByKey()                     // Better to use groupByKey() if stream is already keyed by driverId
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(2)))
                .count(Materialized.as("overspeeding-count-2min"));
    }

    private void routeByVehicleType(KStream<String, LocationTelemetry> deliveries) {
        deliveries.split()
                .branch((key, location) -> "CAR".equals(location.getVehicleType()),
                        Branched.withConsumer(carStream -> carStream.to(AppConstants.CAR_DELIVERIES, Produced.with(Serdes.String(), locationSerde))))
                .branch((key, location) -> "BIKE".equals(location.getVehicleType()),
                        Branched.withConsumer(bikeStream -> bikeStream.to(AppConstants.BIKE_DELIVERIES, Produced.with(Serdes.String(), locationSerde))))
                .branch((key, location) -> "TRUCK".equals(location.getVehicleType()),
                        Branched.withConsumer(truckStream -> truckStream.to(AppConstants.TRUCK_DELIVERIES, Produced.with(Serdes.String(), locationSerde))))
                .defaultBranch(Branched.withConsumer(otherStream -> otherStream.to("OTHER_DELIVERIES", Produced.with(Serdes.String(), locationSerde))));

    }

    private void publishDriverStatus(KStream<String, LocationTelemetry> deliveries) {
        KStream<String, DriverStatus> driverStatusStream = deliveries.mapValues(location -> {
            try {
                String vehicleType = location.getVehicleType();
                if ("TRUCK".equals(vehicleType) && location.getSpeed() > 50) {
                    throw new RuntimeException("DRUNK Driver!");
                }
                return DriverStatus.builder()
                        .driverId(location.getDriverId())
                        .status(location.getSpeed() != null && location.getSpeed() > 0 ? "MOVING" : "STOPPED")
                        .build();
            } catch (Exception e) {
                // Log the error and return null. We will filter these out later.
                log.error("Error creating DriverStatus for driver {}: {}", location.getDriverId(), e.getMessage());
                return null;
            }
        });

        driverStatusStream.to(AppConstants.DRIVER_STATUS_UPDATES, Produced.with(Serdes.String(), driverStatusSerde));

//        // Split the stream into good and bad records
//        KStream<String, DriverStatus>[] branches = driverStatusStream
//                .branch(
//                        (key, value) -> value != null,
//                        (key, value) -> value == null
//                );
//
//        // Send good records to the status topic
//        branches[0].to(AppConstants.DRIVER_STATUS_UPDATES, Produced.with(Serdes.String(), driverStatusSerde));
//        branches[1].to(AppConstants.DRIVER_STATUS_UPDATES+".DLT", Produced.with(Serdes.String(), driverStatusSerde));
    }

}
