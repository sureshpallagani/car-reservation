package com.carrental.domain;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a reservation",
        example = "{\"carType\":\"SEDAN\",\"startDateTime\":\"2026-06-10T10:00:00\",\"numberOfDays\":3}")
public class ReservationRequest {

    @Schema(description = "Selected car type", example = "SEDAN", required = true)
    private CarType carType;

    @Schema(description = "Reservation start date and time", format = "date-time", example = "2026-06-10T10:00:00", required = true)
    private LocalDateTime startDateTime;

    @Schema(description = "Number of days to reserve", example = "3", required = true)
    private int numberOfDays;

}