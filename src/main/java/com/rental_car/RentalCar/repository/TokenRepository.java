package com.rental_car.RentalCar.repository;

import com.rental_car.RentalCar.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByEmail(String email);
}
