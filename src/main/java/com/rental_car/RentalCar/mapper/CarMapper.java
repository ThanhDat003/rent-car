package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.Car;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CarMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public CarDTO toDTO(Car car){
        return modelMapper.map(car, CarDTO.class);
    }

    public Car toEntity(CarDTO carDTO){
        return modelMapper.map(carDTO, Car.class);
    }
}
