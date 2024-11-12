package com.rental_car.RentalCar.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendActivationEmail(String email, String activationLink) throws MessagingException;
    void sendResetPasswordEmail(String email, String resetPasswordLink) throws MessagingException;
}
