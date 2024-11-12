package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.PasswordResetToken;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.PasswordResetTokenRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ForgotPasswordService;
import com.rental_car.RentalCar.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private MailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void forgotPassword(String email){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + email);
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(24));
        tokenRepository.save(resetToken);
        String resetLink = "http://localhost:11111/rent-car/reset-password?token=" + token;
        try {
            emailService.sendResetPasswordEmail(email, resetLink);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String token, String password) {
        PasswordResetToken resetToken = findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired password reset token.");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
