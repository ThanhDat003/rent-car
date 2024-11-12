package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Token;
import com.rental_car.RentalCar.repository.TokenRepository;
import com.rental_car.RentalCar.service.ActivateAccount;
import com.rental_car.RentalCar.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActivateAccountImpl implements ActivateAccount {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MailService mailService;

    @Override
    public void activateAccount(String email, String password) throws MessagingException {
        Token tokencheck = tokenRepository.findByEmail(email);
        if (tokencheck == null) {
            throw new IllegalArgumentException("No email found");
        }
        String token = UUID.randomUUID().toString();
        tokencheck.setToken(token);
        tokencheck.setExpiredDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(tokencheck);
        try {
            mailService.sendActivationEmail(email, "http://localhost:11111/rent-car/signup/activate?token=" + token + "&email=" + email + "&password=" + password);
        }catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
