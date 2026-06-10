package com.carrental.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.domain.Reservation;
import com.carrental.domain.ReservationRequest;
import com.carrental.domain.ReservationResponse;
import com.carrental.service.ReservationService;

@DisplayName("ReservationController Tests")
@ExtendWith(MockitoExtension.class)
class ReservationControllerTests {

    @Mock
    private ReservationService reservationService;

    private ReservationController controller;

    @BeforeEach
    void setUp() {
        controller = new ReservationController(reservationService);
    }

    @Test
    @DisplayName("should create reservation and return reservation response")
    void testReserveCreatesReservation() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 10, 10, 0);
        LocalDateTime end = start.plusDays(3);
        Car car = new Car(1, CarType.SEDAN);
        Reservation reservation = new Reservation(1001, car, start, end);

        when(reservationService.reserve(
                eq(CarType.SEDAN),
                eq(start),
                eq(3)))
                .thenReturn(reservation);

        ReservationRequest request = new ReservationRequest(
                CarType.SEDAN,
                start,
                3);

        ReservationResponse response = controller.reserve(request);

        assertEquals(1001, response.getReservationId());
        assertEquals(1, response.getCarId());
        assertEquals(CarType.SEDAN, response.getCarType());
        assertEquals(start, response.getStartDateTime());
        assertEquals(end, response.getEndDateTime());
    }

    @Test
    @DisplayName("should return all reservation responses")
    void testGetAllReservationsReturnsReservationResponses() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 10, 10, 0);
        LocalDateTime end = start.plusDays(2);
        Car car = new Car(2, CarType.VAN);
        Reservation reservation = new Reservation(1002, car, start, end);

        when(reservationService.getAllReservations())
                .thenReturn(List.of(reservation));

        var responses = controller.getAllReservations();

        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(1002, response.getReservationId());
        assertEquals(2, response.getCarId());
        assertEquals(CarType.VAN, response.getCarType());
        assertEquals(start, response.getStartDateTime());
        assertEquals(end, response.getEndDateTime());
    }
}
