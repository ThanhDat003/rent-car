package com.rental_car.RentalCar.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private boolean is_complete_profile;
    private Car_OwnerDTO car_ownerDTO;
    private CustomerDTO customerDTO;
}

