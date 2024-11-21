package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.repository.FeedbackRepository;
import com.rental_car.RentalCar.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public double calculateRating(Long carId) {
        List<Integer> ratings = feedbackRepository.findRatingByCarId(carId);
        return ratings.isEmpty() ? 0 : ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
    }
}
