package com.rental_car.RentalCar.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ProfileDTO {
    private String name;
    private LocalDate dateOfBirth;
    private String nationalIdNo;
    private String phoneNo;
    private String email;
    private String address;
    private String drivingLicense;
}
