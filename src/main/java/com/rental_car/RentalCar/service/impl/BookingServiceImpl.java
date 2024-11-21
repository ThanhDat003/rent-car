package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.Payment;
import com.rental_car.RentalCar.repository.BookingRepository;
import com.rental_car.RentalCar.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Integer calculateDays(LocalDateTime start_date_time, LocalDateTime end_date_time) {
        Duration duration = Duration.between(start_date_time, end_date_time);
        int days = (int) duration.toDays();
        return days;
    }

    @Override
    public Long calculateTotalPrice(LocalDateTime start_date_time, LocalDateTime end_date_time, String price) {
        Integer days = calculateDays(start_date_time, end_date_time);
        Long totalPrice = days * Long.parseLong(price);
        return totalPrice;
    }

    @Override
    public Booking paymentPaypal(String driver_information, LocalDateTime start_date_time, LocalDateTime end_date_time, Car car, Customer customer, double totalPrice) {
        Booking booking = new Booking();
        booking.setStartDateTime(start_date_time);
        booking.setEndDateTime(end_date_time);
        booking.setDriver_information(driver_information);
        booking.setPayment_method("PayPal");
        booking.setStatus("Processing");
        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setTotal_price(totalPrice);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking paymentCash(String dirver_information, LocalDateTime start_date_time, LocalDateTime end_date_time, Car car, Customer customer, double totalPrice) {
        Booking booking = new Booking();
        booking.setStartDateTime(start_date_time);
        booking.setEndDateTime(end_date_time);
        booking.setDriver_information(dirver_information);
        booking.setPayment_method("Cash");
        booking.setStatus("Processing");
        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setTotal_price(totalPrice);
        bookingRepository.save(booking);
        return booking;
    }

}
