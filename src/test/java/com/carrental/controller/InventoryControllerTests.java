package com.carrental.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.service.InventoryService;

@DisplayName("InventoryController Tests")
@ExtendWith(MockitoExtension.class)
class InventoryControllerTests {

    @Mock
    private InventoryService inventoryService;

    private InventoryController controller;

    @BeforeEach
    void setUp() {
        controller = new InventoryController(inventoryService);
    }

    @Test
    @DisplayName("should return Available when a car is available")
    void testAvailabilityReturnsAvailable() {
        when(inventoryService.findAvailableCar(
                eq(CarType.SEDAN),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Car(1, CarType.SEDAN)));

        var response = controller.availability(
                CarType.SEDAN,
                LocalDateTime.of(2026, 6, 10, 10, 0),
                3);

        assertEquals("Available", response.getBody());
    }

    @Test
    @DisplayName("should return Not Available when no car is found")
    void testAvailabilityReturnsNotAvailable() {
        when(inventoryService.findAvailableCar(
                eq(CarType.VAN),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        var response = controller.availability(
                CarType.VAN,
                LocalDateTime.of(2026, 6, 11, 10, 0),
                1);

        assertEquals("Not Available", response.getBody());
    }

    @Test
    @DisplayName("should return available cars summary")
    void testCarsEndpointReturnsAvailableCar() {
        when(inventoryService.findAvailableCar(
                eq(CarType.SEDAN),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Car(1, CarType.SEDAN)));

        var response = controller.cars();

        assertEquals("Available cars: 1 (SEDAN)", response.getBody());
    }
}
