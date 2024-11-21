package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.repository.BookingRepository;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.service.SearchCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchCarServiceImpl implements SearchCarService {

    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public SearchCarServiceImpl(CarRepository carRepository, BookingRepository bookingRepository) {
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Car> searchCarAvailable(String name, LocalDateTime start_date_time, LocalDateTime end_date_time) {
        List<Car> cars = carRepository.findByAddressContainingIgnoreCase(name);
        List<Booking> bookings = bookingRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(start_date_time, end_date_time);

        List<Long> unavailableCarIds = bookings.stream()
                .map(booking -> booking.getCar().getId())
                .collect(Collectors.toList());

        return cars.stream()
                .filter(car -> !unavailableCarIds.contains(car.getId()))
                .collect(Collectors.toList());
    }
}
