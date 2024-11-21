package com.rental_car.RentalCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 30)
    private String name;
    @Column(name = "license_plate", length = 50, unique = true)
    private String license_plate;
    @Column(name = "brand", length = 50)
    private String brand;
    @Column(name = "model", length = 50)
    private String model;
    @Column(name = "color", length = 15)
    private String color;
    @Column(name = "number_of_seats")
    private int number_of_seats;
    @Column(name = "production_years", length = 10)
    private String production_years;
    @Column(name = "transmission_type", length = 20)
    private String transmission_type;
    @Column(name = "fuel_type", length = 20)
    private String fuel_type;
    @Column(name = "mileage")
    private Double mileage;
    @Column(name = "fuel_consumption")
    private Double fuel_consumption;
    @Column(name = "base_price")
    private Long base_price;
    @Column(name = "deposit")
    private Double deposit;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;
    @Column(name = "additional_functions")
    private String additional_functions;
    @Column(name = "terms_of_use")
    private String terms_of_use;
    @Column(name = "images")
    private String images;
    @ManyToOne
    @JoinColumn(name = "car_owner_id")
    private Car_Owner car_owner;
}
