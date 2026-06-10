package com.carrental.domain;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response body for a created reservation",
        example = "{\"reservationId\":1001,\"carId\":1,\"carType\":\"SEDAN\",\"startDateTime\":\"2026-06-10T10:00:00\",\"endDateTime\":\"2026-06-13T10:00:00\"}")
public class ReservationResponse {

    @Schema(description = "Unique reservation identifier", example = "1001")
    private Integer reservationId;

    @Schema(description = "Identifier of the reserved car", example = "1")
    private Integer carId;

    @Schema(description = "Reserved car type", example = "SEDAN")
    private CarType carType;

    @Schema(description = "Reservation start date and time", format = "date-time", example = "2026-06-10T10:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "Reservation end date and time", format = "date-time", example = "2026-06-13T10:00:00")
    private LocalDateTime endDateTime;

}