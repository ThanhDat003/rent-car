package com.rental_car.RentalCar.dto;

import com.rental_car.RentalCar.entity.Booking;
import lombok.Data;

@Data
public class FeedbackDTO {
    private Long id;
    private int ratings;
    private String content;
    private Booking booking;
}
