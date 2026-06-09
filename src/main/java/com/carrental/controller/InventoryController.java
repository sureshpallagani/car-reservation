package com.carrental.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.domain.CarType;
import com.carrental.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory", description = "Inventory operations")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(
            InventoryService service) {

        this.service = service;
    }

    @Operation(
            summary = "Check car availability",
            description = "Check if a car of the specified type is available for the given period"
    )
    @GetMapping("/availability")
    public ResponseEntity<String> availability(
            @Parameter(description = "Type of car to check", required = true)
            @RequestParam CarType carType,
            @Parameter(description = "Reservation start date and time in ISO 8601 format", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime startDateTime,
            @Parameter(description = "Number of days for the reservation", required = true)
            @RequestParam Integer days) {

        boolean available =
                service.findAvailableCar(
                                carType,
                                startDateTime,
                                startDateTime.plusDays(days))
                        .isPresent();

        return ResponseEntity.ok(
                available
                        ? "Available"
                        : "Not Available");
    }

    @Operation(
            summary = "Get available cars",
            description = "Get list of available cars for the next day"
    )
    @GetMapping("/cars")
    public ResponseEntity<String> cars() {

        return ResponseEntity.ok(
                "Available cars: " +
                        service.findAvailableCar(
                                        CarType.SEDAN,
                                        LocalDateTime.now(),
                                        LocalDateTime.now().plusDays(1))
                                .map(car -> car.getCarId() + " (" + car.getCarType() + ")")
                                .orElse("None"));
    }
}