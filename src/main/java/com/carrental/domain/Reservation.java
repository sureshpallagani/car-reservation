package com.carrental.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    private Integer reservationId;
    private Car car;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public boolean overlaps(
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd) {

        return requestedStart.isBefore(endDateTime)
                && requestedEnd.isAfter(startDateTime);
    }

}