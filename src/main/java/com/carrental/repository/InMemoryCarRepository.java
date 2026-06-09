package com.carrental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;

@Repository
public class InMemoryCarRepository
        implements CarRepository {

    private final List<Car> cars = List.of(
            new Car(1, CarType.SEDAN),
            new Car(2, CarType.SEDAN),
            new Car(3, CarType.SUV),
            new Car(4, CarType.SUV),
            new Car(5, CarType.VAN)
    );

    @Override
    public List<Car> findByType(CarType carType) {

        return cars.stream()
                .filter(c -> c.getCarType() == carType)
                .toList();
    }

    @Override
    public Optional<Car> findById(Integer id) {

        return cars.stream()
                .filter(c -> c.getCarId().equals(id))
                .findFirst();
    }
}