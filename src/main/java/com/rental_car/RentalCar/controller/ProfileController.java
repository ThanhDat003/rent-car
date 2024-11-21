package com.rental_car.RentalCar.controller;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.ProfileMapper;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.CustomerRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rent-car")
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private Car_OwnerRepository carOwnerRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/owner/profile")
    public String profileOwner(Model model) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name = carOwner.getName();
            model.addAttribute("name", name);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name = customer.getName();
            model.addAttribute("name", name);
        }

        Car_Owner carOwner = carOwnerRepository.findByEmail(email);
        System.out.println(carOwner.getName());
        System.out.println(carOwner.getDate_of_birth());
        System.out.println(carOwner.getNational_id_no());
        model.addAttribute("profile", profileMapper.toDTO2(carOwner));

        return "car-owner/profile";
    }

    @PostMapping("/owner/profile")
    public String updateProfile(@ModelAttribute("profile") ProfileDTO profileDTO,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name = carOwner.getName();
            model.addAttribute("name", name);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name = customer.getName();
            model.addAttribute("name", name);
        }

        System.out.println(profileDTO.getEmail().equals(user.getEmail())+"check");

        try {
            if (!profileDTO.getEmail().equals(user.getEmail())) {
                profileService.updateProfile(user, profileDTO);
                redirectAttributes.addFlashAttribute("successReset", "Email changed successfully. Please log in again.");

                SecurityContextHolder.clearContext();
                return "redirect:/rent-car/login";
            }
            profileService.updateProfile(user, profileDTO);
            model.addAttribute("success", "Profile updated successfully.");
            return "car-owner/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile. Please try again.");
            return "car-owner/profile";
        }


    }

    @GetMapping("/customer/profile")
    public String profileCustomer(Model model) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name = carOwner.getName();
            model.addAttribute("name", name);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name = customer.getName();
            model.addAttribute("name", name);
        }

        Customer customer = customerRepository.findByEmail(email);
        model.addAttribute("profile", profileMapper.toDTO1(customer));

        return "customer/profile";
    }

    @PostMapping("/customer/profile")
    public String updateProfileCustomer(@ModelAttribute("profile") ProfileDTO profileDTO,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name = carOwner.getName();
            model.addAttribute("name", name);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name = customer.getName();
            model.addAttribute("name", name);
        }

        System.out.println(profileDTO.getEmail().equals(user.getEmail())+"check");

        try {
            if (!profileDTO.getEmail().equals(user.getEmail())) {
                profileService.updateProfile(user, profileDTO);
                redirectAttributes.addFlashAttribute("successReset", "Email changed successfully. Please log in again.");

                SecurityContextHolder.clearContext();
                return "redirect:/rent-car/login";
            }
            profileService.updateProfile(user, profileDTO);
            model.addAttribute("success", "Profile updated successfully.");
            return "customer/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile. Please try again.");
            return "customer/profile";
        }

    }
}
