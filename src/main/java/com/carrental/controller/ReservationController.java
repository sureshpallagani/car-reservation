package com.carrental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.domain.Reservation;
import com.carrental.domain.ReservationRequest;
import com.carrental.domain.ReservationResponse;
import com.carrental.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
            description = "Reserve a car of the requested type for the given start date and number of days",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservationRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "reservationRequest",
                                            value = "{\"carType\": \"SEDAN\", \"startDateTime\": \"2026-06-10T10:00:00\", \"numberOfDays\": 3}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reservation created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "reservationResponse",
                                                    value = "{\"reservationId\": 1001, \"carId\": 1, \"carType\": \"SEDAN\", \"startDateTime\": \"2026-06-10T10:00:00\", \"endDateTime\": \"2026-06-13T10:00:00\"}"
                                            )
                                    }
                            )
                    )
            }
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

    @Operation(
            summary = "List all reservations",
            description = "Fetch all existing reservations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of all reservations",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = ReservationResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public java.util.List<ReservationResponse> getAllReservations() {
        return service.getAllReservations().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getReservationId(),
                        reservation.getCar().getCarId(),
                        reservation.getCar().getCarType(),
                        reservation.getStartDateTime(),
                        reservation.getEndDateTime()))
                .toList();
    }
}
