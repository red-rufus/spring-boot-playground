package com.eventdriven.app.serde;

import com.eventdriven.app.dto.LocationTelemetry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class LocationDeserializer implements Deserializer<LocationTelemetry> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LocationTelemetry deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(
                    data,
                    LocationTelemetry.class
            );
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Error deserializing Location",
                    e
            );
        }
    }
}
