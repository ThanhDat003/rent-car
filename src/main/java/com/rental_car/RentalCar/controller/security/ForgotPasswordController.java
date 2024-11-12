package com.rental_car.RentalCar.controller.security;

import com.rental_car.RentalCar.entity.PasswordResetToken;
import com.rental_car.RentalCar.repository.PasswordResetTokenRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rent-car")
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "security/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        if (userRepository.findByEmail(email).getId() == tokenRepository.findAll().get(0).getUser().getId()) {
            model.addAttribute("errorMessage", "Please check your email as we have sent you a password reset link earlier");
            return "security/forgot-password";
        }
        try {
            userService.forgotPassword(email);
            //model.addAttribute("success", "A reset password link has been sent to your email.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "security/forgot-password";
        }
        return "redirect:/rent-car/forgot-password?success";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "security/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            model.addAttribute("token", token);
            return "security/reset-password";
        }

        try {
            userService.resetPassword(token, password);
            model.addAttribute("successReset", "Your password has been reset successfully.");
            return "security/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Invalid or expired password reset token.");
            model.addAttribute("token", token);
            return "security/reset-password";
        }
    }
}