package com.eventdriven.app.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DriverStatus {
    private String driverId;
    private String status;
}
