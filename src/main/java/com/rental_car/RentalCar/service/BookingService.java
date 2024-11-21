package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.Payment;

import java.time.LocalDateTime;

public interface BookingService {
    Integer calculateDays(LocalDateTime start_date_time, LocalDateTime end_date_time);
    Long calculateTotalPrice(LocalDateTime start_date_time, LocalDateTime end_date_time, String price);
    Booking paymentPaypal(String location, LocalDateTime start_date_time, LocalDateTime end_date_time, Car car, Customer customer, double totalPrice);
    Booking paymentCash(String location, LocalDateTime start_date_time, LocalDateTime end_date_time, Car car, Customer customer, double totalPrice);
}
