package com.rental_car.RentalCar.dto;

import com.rental_car.RentalCar.entity.Car_Owner;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDTO {
    private Long id;
    private String name;
    private String license_plate;
    private String brand;
    private String model;
    private String color;
    private int number_of_seats;
    private String production_years;
    private String transmission_type;
    private String fuel_type;
    private Double mileage;
    private Double fuel_consumption;
    private Long base_price;
    private Double deposit;
    private String address;
    private String description;
    private String additional_functions;
    private String terms_of_use;
    private String images;
    private Car_Owner car_owner;
}
