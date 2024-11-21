package com.rental_car.RentalCar.repository;

import com.rental_car.RentalCar.entity.Booking;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime start, LocalDateTime end);
    List<Booking> findAllByCustomerOrderByIdDesc(Customer customer);
    Booking findByCar(Car car);

    List<Booking> findByCarId(Long id);
}
