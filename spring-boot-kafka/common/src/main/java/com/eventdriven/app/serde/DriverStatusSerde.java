package com.eventdriven.app.serde;

import com.eventdriven.app.dto.DriverStatus;
import org.apache.kafka.common.serialization.Serdes;

public class DriverStatusSerde extends Serdes.WrapperSerde<DriverStatus>{

    public DriverStatusSerde() {
        super(new DriverStatusSerializer(), new DriverStatusDeserializer());
    }
}
