package com.rental_car.RentalCar.dto;

import lombok.Data;

@Data
public class Car_OwnerDTO {
    private Long id;
    private String name;
    private String date_of_birth;
    private String national_id_no;
    private String phone_no;
    private String email;
    private String address;
    private String driving_license;
    private Double wallet;
}
