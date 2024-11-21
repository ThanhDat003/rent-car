package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.entity.Car;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCarService {
    List<Car> searchCarAvailable(String name, LocalDateTime start_date_time, LocalDateTime end_date_time);
}
