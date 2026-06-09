package com.carrental.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.carrental.domain.Car;
import com.carrental.domain.CarType;
import com.carrental.domain.Reservation;

@DisplayName("InMemoryReservationRepository Tests")
class InMemoryReservationRepositoryTests {

    private ReservationRepository reservationRepository;
    private Car testCar;
    private LocalDateTime now;
    private LocalDateTime later;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        testCar = new Car(1, CarType.SEDAN);
        now = LocalDateTime.now();
        later = now.plusDays(3);
    }

    @Test
    @DisplayName("should save a reservation")
    void testSaveReservation() {
        Reservation reservation = new Reservation(1, testCar, now, later);
        reservationRepository.save(reservation);
        
        List<Reservation> all = reservationRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(1, all.get(0).getReservationId());
    }

    @Test
    @DisplayName("should save multiple reservations")
    void testSaveMultipleReservations() {
        Reservation res1 = new Reservation(1, testCar, now, later);
        Reservation res2 = new Reservation(2, testCar, later.plusDays(1), later.plusDays(4));
        
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        
        List<Reservation> all = reservationRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("should find reservations by car ID")
    void testFindByCarId() {
        Car car2 = new Car(2, CarType.SUV);
        Reservation res1 = new Reservation(1, testCar, now, later);
        Reservation res2 = new Reservation(2, car2, now, later);
        
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        
        List<Reservation> carReservations = 
            reservationRepository.findByCarId(1);
        assertEquals(1, carReservations.size());
        assertEquals(1, carReservations.get(0).getCar().getCarId());
    }

    @Test
    @DisplayName("should return empty list when no reservations for car")
    void testFindByCarIdNotFound() {
        List<Reservation> reservations = 
            reservationRepository.findByCarId(999);
        assertTrue(reservations.isEmpty());
    }

    @Test
    @DisplayName("should find reservation by ID")
    void testFindById() {
        Reservation reservation = new Reservation(100, testCar, now, later);
        reservationRepository.save(reservation);
        
        Optional<Reservation> found = 
            reservationRepository.findById(100);
        assertTrue(found.isPresent());
        assertEquals(100, found.get().getReservationId());
    }

    @Test
    @DisplayName("should return empty Optional when reservation not found")
    void testFindByIdNotFound() {
        Optional<Reservation> found = 
            reservationRepository.findById(999);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("should return all reservations")
    void testFindAll() {
        Reservation res1 = new Reservation(1, testCar, now, later);
        Reservation res2 = new Reservation(2, testCar, later.plusDays(1), later.plusDays(4));
        
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        
        List<Reservation> all = reservationRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("should return empty list when no reservations exist")
    void testFindAllEmpty() {
        List<Reservation> all = reservationRepository.findAll();
        assertTrue(all.isEmpty());
    }
}
