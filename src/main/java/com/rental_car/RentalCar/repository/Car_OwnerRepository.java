package com.rental_car.RentalCar.repository;

import com.rental_car.RentalCar.entity.Car_Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Car_OwnerRepository extends JpaRepository<Car_Owner, Long> {
    Car_Owner findByEmail(String email);
}
