package com.carrental.repository;

import java.util.List;
import java.util.Optional;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;

public interface CarRepository {

    List<Car> findByType(CarType carType);

    Optional<Car> findById(Integer id);
}