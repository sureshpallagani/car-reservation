package com.carrental.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;

@DisplayName("InMemoryCarRepository Tests")
class InMemoryCarRepositoryTests {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new InMemoryCarRepository();
    }

    @Test
    @DisplayName("should find cars by type SEDAN")
    void testFindByTypeSedan() {
        List<Car> sedans = carRepository.findByType(CarType.SEDAN);
        assertEquals(2, sedans.size());
        assertTrue(sedans.stream()
                .allMatch(c -> c.getCarType() == CarType.SEDAN));
    }

    @Test
    @DisplayName("should find cars by type SUV")
    void testFindBySUV() {
        List<Car> suvs = carRepository.findByType(CarType.SUV);
        assertEquals(2, suvs.size());
        assertTrue(suvs.stream()
                .allMatch(c -> c.getCarType() == CarType.SUV));
    }

    @Test
    @DisplayName("should find cars by type VAN")
    void testFindByVan() {
        List<Car> vans = carRepository.findByType(CarType.VAN);
        assertEquals(1, vans.size());
        assertEquals(CarType.VAN, vans.get(0).getCarType());
    }

    @Test
    @DisplayName("should find car by ID")
    void testFindById() {
        Optional<Car> car = carRepository.findById(1);
        assertTrue(car.isPresent());
        assertEquals(1, car.get().getCarId());
        assertEquals(CarType.SEDAN, car.get().getCarType());
    }

    @Test
    @DisplayName("should return empty Optional for non-existent car ID")
    void testFindByIdNotFound() {
        Optional<Car> car = carRepository.findById(999);
        assertTrue(car.isEmpty());
    }

    @Test
    @DisplayName("should return all cars have correct IDs")
    void testFindByIdMultiple() {
        for (int i = 1; i <= 5; i++) {
            Optional<Car> car = carRepository.findById(i);
            assertTrue(car.isPresent());
            assertEquals(i, car.get().getCarId());
        }
    }
}
