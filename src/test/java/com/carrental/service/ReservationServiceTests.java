package com.carrental.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.domain.Reservation;
import com.carrental.exception.NoCarAvailableException;
import com.carrental.repository.ReservationRepository;

@DisplayName("ReservationService Tests")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTests {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;
    private LocalDateTime startDate;
    private Car availableCar;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                inventoryService,
                reservationRepository);
        startDate = LocalDateTime.of(2026, 6, 10, 10, 0);
        availableCar = new Car(1, CarType.SEDAN);
    }

    @Test
    @DisplayName("should create reservation successfully when car is available")
    void testReserveSuccessfully() {
        when(inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                startDate.plusDays(3)))
                .thenReturn(Optional.of(availableCar));

        Reservation reservation = reservationService.reserve(
                CarType.SEDAN,
                startDate,
                3);

        assertNotNull(reservation);
        assertEquals(availableCar.getCarId(), reservation.getCar().getCarId());
        assertEquals(startDate, reservation.getStartDateTime());
        assertEquals(startDate.plusDays(3), reservation.getEndDateTime());
        assertNotNull(reservation.getReservationId());

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(1)).save(captor.capture());
        assertEquals(reservation.getReservationId(), captor.getValue().getReservationId());
    }

    @Test
    @DisplayName("should throw exception when no car available")
    void testReserveNoCarAvailable() {
        when(inventoryService.findAvailableCar(
                CarType.SUV,
                startDate,
                startDate.plusDays(1)))
                .thenReturn(Optional.empty());

        assertThrows(NoCarAvailableException.class, () ->
                reservationService.reserve(
                        CarType.SUV,
                        startDate,
                        1));

        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("should calculate correct end date with 5 days")
    void testReserveWithMultipleDays() {
        when(inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                startDate.plusDays(5)))
                .thenReturn(Optional.of(availableCar));

        Reservation reservation = reservationService.reserve(
                CarType.SEDAN,
                startDate,
                5);

        assertEquals(startDate.plusDays(5), reservation.getEndDateTime());
    }

    @Test
    @DisplayName("should generate unique reservation IDs")
    void testReservationIdUniqueness() {
        when(inventoryService.findAvailableCar(
                eq(CarType.SEDAN),
                any(),
                any()))
                .thenReturn(Optional.of(availableCar));

        Reservation res1 = reservationService.reserve(
                CarType.SEDAN,
                startDate,
                1);
        Reservation res2 = reservationService.reserve(
                CarType.SEDAN,
                startDate.plusDays(2),
                1);

        assertNotEquals(res1.getReservationId(), res2.getReservationId());
        assertTrue(res2.getReservationId() > res1.getReservationId());
    }

    @Test
    @DisplayName("should save reservation to repository")
    void testReservationSavedToRepository() {
        when(inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                startDate.plusDays(2)))
                .thenReturn(Optional.of(availableCar));

        reservationService.reserve(
                CarType.SEDAN,
                startDate,
                2);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(1)).save(captor.capture());

        Reservation savedReservation = captor.getValue();
        assertEquals(availableCar.getCarId(), savedReservation.getCar().getCarId());
        assertEquals(startDate, savedReservation.getStartDateTime());
        assertEquals(startDate.plusDays(2), savedReservation.getEndDateTime());
    }

    @Test
    @DisplayName("should handle single day reservation")
    void testReserveSingleDay() {
        when(inventoryService.findAvailableCar(
                CarType.VAN,
                startDate,
                startDate.plusDays(1)))
                .thenReturn(Optional.of(new Car(5, CarType.VAN)));

        Reservation reservation = reservationService.reserve(
                CarType.VAN,
                startDate,
                1);

        assertEquals(startDate, reservation.getStartDateTime());
        assertEquals(startDate.plusDays(1), reservation.getEndDateTime());
    }

    @Test
    @DisplayName("should invoke inventoryService with correct parameters")
    void testInventoryServiceInvoked() {
        when(inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                startDate.plusDays(3)))
                .thenReturn(Optional.of(availableCar));

        reservationService.reserve(
                CarType.SEDAN,
                startDate,
                3);

        verify(inventoryService, times(1)).findAvailableCar(
                CarType.SEDAN,
                startDate,
                startDate.plusDays(3));
    }

    @Test
    @DisplayName("should return all reservations from repository")
    void testGetAllReservations() {
        Reservation savedReservation = new Reservation(
                1001,
                availableCar,
                startDate,
                startDate.plusDays(3));

        when(reservationRepository.findAll())
                .thenReturn(List.of(savedReservation));

        var reservations = reservationService.getAllReservations();

        assertEquals(1, reservations.size());
        assertEquals(1001, reservations.get(0).getReservationId());
        verify(reservationRepository, times(1)).findAll();
    }
}
