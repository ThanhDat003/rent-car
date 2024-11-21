package com.rental_car.RentalCar.repository;

import com.rental_car.RentalCar.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAddressContainingIgnoreCase(String name);
}
