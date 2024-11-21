package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.repository.BookingRepository;
import com.rental_car.RentalCar.service.ListBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListBookingServiceImpl implements ListBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> listBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().collect(Collectors.toList());
    }
}
