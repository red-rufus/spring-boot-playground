package com.eventdriven.app.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class LocationTelemetry  {
    private String driverId;
    private Double latitude;
    private Double longitude;
    private Long timestamp;
    private Double speed;
    private Double distance;
    private String city;
    private String vehicleType;
    private Boolean deliveryCompleted;
}
