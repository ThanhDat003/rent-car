package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.config.ModelMapperConfig;
import com.rental_car.RentalCar.dto.LoginDTO;
import com.rental_car.RentalCar.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public LoginMapper(ModelMapperConfig modelMapperConfig) {
        this.modelMapper = modelMapperConfig.modelMapper();
    }

    public LoginDTO toLoginDTO(User user) {
        return modelMapper.map(user, LoginDTO.class);
    }

    public User toUser(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }
}
