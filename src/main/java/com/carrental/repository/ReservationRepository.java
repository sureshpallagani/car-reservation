package com.carrental.repository;

import java.util.List;
import java.util.Optional;

import com.carrental.domain.Reservation;

public interface ReservationRepository {

    void save(Reservation reservation);

    List<Reservation> findByCarId(Integer carId);

    Optional<Reservation> findById(Integer reservationId);

    List<Reservation> findAll();
}