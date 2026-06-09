package com.carrental.service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.domain.Reservation;
import com.carrental.exception.NoCarAvailableException;
import com.carrental.repository.ReservationRepository;

@Service
public class ReservationService {

    private final InventoryService inventoryService;
    private final ReservationRepository reservationRepository;

    private final AtomicInteger idGenerator =
            new AtomicInteger(1000);

    public ReservationService(
            InventoryService inventoryService,
            ReservationRepository reservationRepository) {

        this.inventoryService = inventoryService;
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserve(
            CarType type,
            LocalDateTime start,
            int days) {

        LocalDateTime end =
                start.plusDays(days);

        Car car =
                inventoryService
                        .findAvailableCar(
                                type,
                                start,
                                end)
                        .orElseThrow(
                                NoCarAvailableException::new);

        Reservation reservation =
                new Reservation(
                        idGenerator.incrementAndGet(),
                        car,
                        start,
                        end);

        reservationRepository.save(
                reservation);

        return reservation;
    }
}