package com.carrental.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.domain.Reservation;
import com.carrental.domain.ReservationRequest;
import com.carrental.domain.ReservationResponse;
import com.carrental.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "Reservation operations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(
            ReservationService service) {

        this.service = service;
    }

    @Operation(
            summary = "Create a new reservation",
            description = "Reserve a car of the requested type for the given start date and number of days"
    )
    @PostMapping
    public ReservationResponse reserve(
            @RequestBody
            ReservationRequest request) {

        Reservation reservation =
                service.reserve(
                        request.getCarType(),
                        request.getStartDateTime(),
                        request.getNumberOfDays());

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getCar().getCarId(),
                reservation.getCar().getCarType(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime());
    }
}