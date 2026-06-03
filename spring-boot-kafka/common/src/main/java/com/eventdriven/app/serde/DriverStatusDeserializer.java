package com.eventdriven.app.serde;

import com.eventdriven.app.dto.DriverStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class DriverStatusDeserializer implements Deserializer<DriverStatus> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DriverStatus deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(
                    data,
                    DriverStatus.class
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
