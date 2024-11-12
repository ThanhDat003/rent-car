package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendActivationEmail(String email, String activationLink){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Activate your account");
            helper.setText("Dear " + email + ",\n\n" +
                    "Please click the link below to activate your account:\n\n" +
                    activationLink + "\n\n" +
                    "After activation account, you should delete this email to protect information of your account.\n\n" +
                    "Best regards,\n" +
                    "Rent a Car Team");
            javaMailSender.send(message);
            System.out.println("Activation email sent to " + email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendResetPasswordEmail(String email, String resetPasswordLink) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Reset your password");
        helper.setText("Dear " + email + ",\n\n" +
                "Please click the link below to reset your password:\n\n" +
                resetPasswordLink + "\n\n" +
                "Best regards,\n" +
                "Rent a Car Team");
        javaMailSender.send(message);
    }
}
