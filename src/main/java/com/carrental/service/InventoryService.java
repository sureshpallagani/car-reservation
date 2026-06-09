package com.carrental.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;

@Service
public class InventoryService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public InventoryService(
            CarRepository carRepository,
            ReservationRepository reservationRepository) {

        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    public Optional<Car> findAvailableCar(
            CarType type,
            LocalDateTime start,
            LocalDateTime end) {

        List<Car> cars =
                carRepository.findByType(type);

        for (Car car : cars) {

            boolean available =
                    reservationRepository
                            .findByCarId(car.getCarId())
                            .stream()
                            .noneMatch(r ->
                                    r.overlaps(start, end));

            if (available) {
                return Optional.of(car);
            }
        }

        return Optional.empty();
    }
}