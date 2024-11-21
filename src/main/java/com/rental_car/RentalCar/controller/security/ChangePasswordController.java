package com.rental_car.RentalCar.controller.security;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.mapper.ProfileMapper;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.service.ChangePasswordService;
import com.rental_car.RentalCar.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rent-car/owner")
public class ChangePasswordController {

    @Autowired
    private ChangePasswordService changePasswordService;
    @Autowired
    private Car_OwnerRepository carOwnerRepository;
    @Autowired
    private ProfileMapper profileMapper;

    @GetMapping("/change-password")
    public String showChangePassword() {
        return "car-owner/profile";
    }

    @PostMapping("/change-password")
    public String changePasswordOwner(@RequestParam("password") String password,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            changePasswordService.changePassword(password);
            redirectAttributes.addFlashAttribute("successReset", "Password changed successfully. Please log in again.");
            SecurityContextHolder.clearContext();
            return "redirect:/rent-car/login";
        } catch (Exception e) {
            model.addAttribute("errorChange", "Error occurred while changing password. Please try again.");
            return "car-owner/profile";
        }
    }
}
