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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.domain.Reservation;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;

@DisplayName("InventoryService Tests")
@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private InventoryService inventoryService;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(
                carRepository,
                reservationRepository);
        startDate = LocalDateTime.of(2026, 6, 10, 10, 0);
        endDate = LocalDateTime.of(2026, 6, 15, 10, 0);
    }

    @Test
    @DisplayName("should find available car when no reservations exist")
    void testFindAvailableCarNoReservations() {
        Car sedan = new Car(1, CarType.SEDAN);
        when(carRepository.findByType(CarType.SEDAN))
                .thenReturn(List.of(sedan));
        when(reservationRepository.findByCarId(1))
                .thenReturn(List.of());

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                endDate);

        assertTrue(available.isPresent());
        assertEquals(1, available.get().getCarId());
        verify(carRepository, times(1)).findByType(CarType.SEDAN);
        verify(reservationRepository, times(1)).findByCarId(1);
    }

    @Test
    @DisplayName("should return empty when no cars of type exist")
    void testFindAvailableCarTypeNotFound() {
        when(carRepository.findByType(CarType.SUV))
                .thenReturn(List.of());

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SUV,
                startDate,
                endDate);

        assertTrue(available.isEmpty());
        verify(carRepository, times(1)).findByType(CarType.SUV);
    }

    @Test
    @DisplayName("should skip car with overlapping reservation")
    void testFindAvailableCarWithOverlappingReservation() {
        Car sedan1 = new Car(1, CarType.SEDAN);
        Car sedan2 = new Car(2, CarType.SEDAN);

        LocalDateTime overlapStart = startDate.plusDays(1);
        LocalDateTime overlapEnd = endDate.minusDays(1);
        Reservation overlapping = new Reservation(
                100,
                sedan1,
                overlapStart,
                overlapEnd);

        when(carRepository.findByType(CarType.SEDAN))
                .thenReturn(List.of(sedan1, sedan2));
        when(reservationRepository.findByCarId(1))
                .thenReturn(List.of(overlapping));
        when(reservationRepository.findByCarId(2))
                .thenReturn(List.of());

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                endDate);

        assertTrue(available.isPresent());
        assertEquals(2, available.get().getCarId());
    }

    @Test
    @DisplayName("should return first available car when multiple exist")
    void testFindAvailableCarMultipleAvailable() {
        Car sedan1 = new Car(1, CarType.SEDAN);
        Car sedan2 = new Car(2, CarType.SEDAN);

        when(carRepository.findByType(CarType.SEDAN))
                .thenReturn(List.of(sedan1, sedan2));
        when(reservationRepository.findByCarId(1))
                .thenReturn(List.of());

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                endDate);

        assertTrue(available.isPresent());
        assertEquals(1, available.get().getCarId());
    }

    @Test
    @DisplayName("should return empty when all cars have conflicting reservations")
    void testFindAvailableCarAllBooked() {
        Car sedan1 = new Car(1, CarType.SEDAN);
        Car sedan2 = new Car(2, CarType.SEDAN);

        Reservation res1 = new Reservation(100, sedan1, startDate, endDate);
        Reservation res2 = new Reservation(101, sedan2, startDate, endDate);

        when(carRepository.findByType(CarType.SEDAN))
                .thenReturn(List.of(sedan1, sedan2));
        when(reservationRepository.findByCarId(1))
                .thenReturn(List.of(res1));
        when(reservationRepository.findByCarId(2))
                .thenReturn(List.of(res2));

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                endDate);

        assertTrue(available.isEmpty());
    }

    @Test
    @DisplayName("should find available car with non-overlapping reservations")
    void testFindAvailableCarWithNonOverlappingReservations() {
        Car sedan = new Car(1, CarType.SEDAN);

        LocalDateTime beforeStart = startDate.minusDays(5);
        LocalDateTime beforeEnd = startDate.minusDays(1);
        Reservation before = new Reservation(100, sedan, beforeStart, beforeEnd);

        LocalDateTime afterStart = endDate.plusDays(1);
        LocalDateTime afterEnd = endDate.plusDays(5);
        Reservation after = new Reservation(101, sedan, afterStart, afterEnd);

        when(carRepository.findByType(CarType.SEDAN))
                .thenReturn(List.of(sedan));
        when(reservationRepository.findByCarId(1))
                .thenReturn(List.of(before, after));

        Optional<Car> available = inventoryService.findAvailableCar(
                CarType.SEDAN,
                startDate,
                endDate);

        assertTrue(available.isPresent());
        assertEquals(1, available.get().getCarId());
    }
}
