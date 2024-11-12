package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.dto.Car_OwnerDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car_OwnerMapper {
    private final ModelMapper modelMapper;
    @Autowired
    public Car_OwnerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Car_OwnerDTO toDTO(Car_Owner car_owner) {
        return modelMapper.map(car_owner, Car_OwnerDTO.class);
    }

    public Car_Owner toEntity(Car_OwnerDTO car_ownerDTO) {
        return modelMapper.map(car_ownerDTO, Car_Owner.class);
    }
}
