package com.rental_car.RentalCar.service;

import jakarta.mail.MessagingException;

public interface ActivateAccount {
    void activateAccount(String email, String password) throws MessagingException;
}
