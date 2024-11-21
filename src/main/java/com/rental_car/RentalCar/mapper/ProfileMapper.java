package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    private final ModelMapper modelMapper;
    @Autowired
    public ProfileMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProfileDTO toDTO1(Customer customer) {
        return modelMapper.map(customer, ProfileDTO.class);
    }

    public Customer toEntity1(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, Customer.class);
    }

    public ProfileDTO toDTO2(Car_Owner car_owner) {
        return modelMapper.map(car_owner, ProfileDTO.class);
    }

    public Car_Owner toEntity2(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, Car_Owner.class);
    }
}
