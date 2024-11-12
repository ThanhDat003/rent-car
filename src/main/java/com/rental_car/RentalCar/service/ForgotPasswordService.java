package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.entity.PasswordResetToken;

public interface ForgotPasswordService {
    void forgotPassword(String email);
    void resetPassword(String token, String password);
    PasswordResetToken findByToken(String token);
}
