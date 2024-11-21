package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.service.CarDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarDetailServiceImpl implements CarDetailService {
    @Autowired
    private CarRepository carRepository;
    @Override
    public Car getCarDetail(Long id) {
        return carRepository.findById(id).orElse(null);
    }
}
