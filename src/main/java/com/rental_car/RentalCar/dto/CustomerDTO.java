package com.rental_car.RentalCar.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private Date date_of_birth;
    private String national_id_no;
    private String phone_no;
    private String email;
    private String address;
    private String driving_license;
    private Double wallet;
}
