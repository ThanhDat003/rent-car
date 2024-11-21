package com.rental_car.RentalCar.repository;

import com.rental_car.RentalCar.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f.ratings FROM Feedback f WHERE f.booking.car.id = :carId")
    List<Integer> findRatingByCarId(@Param("carId") Long carId);
}
