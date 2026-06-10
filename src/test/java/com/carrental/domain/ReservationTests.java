package com.carrental.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Reservation Domain Tests")
class ReservationTests {

    @Test
    @DisplayName("should detect overlapping reservations")
    void testOverlapsReturnsTrueForOverlap() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 10, 10, 0);
        LocalDateTime end = start.plusDays(3);
        Reservation reservation = new Reservation(1, new Car(1, CarType.SEDAN), start, end);

        assertTrue(reservation.overlaps(start.plusDays(1), end.minusDays(1)));
        assertTrue(reservation.overlaps(start, end));
        assertTrue(reservation.overlaps(start.minusHours(1), start.plusHours(1)));
    }

    @Test
    @DisplayName("should not consider adjacent reservations as overlapping")
    void testOverlapsReturnsFalseForAdjacentReservations() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 10, 10, 0);
        LocalDateTime end = start.plusDays(3);
        Reservation reservation = new Reservation(1, new Car(1, CarType.SEDAN), start, end);

        assertFalse(reservation.overlaps(end, end.plusDays(1)));
        assertFalse(reservation.overlaps(start.minusDays(1), start));
    }
}