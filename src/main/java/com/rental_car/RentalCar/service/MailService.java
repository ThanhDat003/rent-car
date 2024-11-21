package com.rental_car.RentalCar.service;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

public interface MailService {
    void sendActivationEmail(String email, String activationLink) throws MessagingException;
    void sendResetPasswordEmail(String email, String resetPasswordLink) throws MessagingException;
    void sendBookingSuccessEmail(String email, String carName, LocalDateTime startDate, LocalDateTime endDate, long price) throws MessagingException;
}
