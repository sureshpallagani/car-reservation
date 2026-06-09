package com.carrental.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carrental.domain.Reservation;

@Repository
public class InMemoryReservationRepository
        implements ReservationRepository {

    private final List<Reservation> reservations =
            new ArrayList<>();

    @Override
    public void save(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByCarId(Integer carId) {

        return reservations.stream()
                .filter(r ->
                        r.getCar()
                         .getCarId()
                         .equals(carId))
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Integer id) {

        return reservations.stream()
                .filter(r ->
                        r.getReservationId()
                         .equals(id))
                .findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }
}