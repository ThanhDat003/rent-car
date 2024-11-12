package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.config.ModelMapperConfig;
import com.rental_car.RentalCar.dto.UserDTO;
import com.rental_car.RentalCar.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
