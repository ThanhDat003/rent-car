package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.User;

public interface CompleteProfileService {
    String completeProfile(User user, ProfileDTO profileDTO);
}
