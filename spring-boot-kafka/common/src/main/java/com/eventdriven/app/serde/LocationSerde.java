package com.eventdriven.app.serde;

import com.eventdriven.app.dto.LocationTelemetry;
import org.apache.kafka.common.serialization.Serdes;

public class LocationSerde extends Serdes.WrapperSerde<LocationTelemetry>{

    public LocationSerde() {
        super(new LocationSerializer(), new LocationDeserializer());
    }
}
