package com.rental_car.RentalCar.controller.security;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.dto.UserDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.CompleteProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rent-car/complete-profile")
public class CompleteProfileController {
    @Autowired
    private CompleteProfileService completeProfileService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public String showCompleteProfileForm(Model model) {
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
        System.out.println("email: " + email);
        if (email != null) {
            user = userRepository.findByEmail(email);
            if (user != null && !user.is_complete_profile()) {
                System.out.println("User found: " + user.getEmail() + " " + user.is_complete_profile());
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(user.getEmail());
                model.addAttribute("profileDTO", new ProfileDTO());
            }
            return "security/complete-profile";
        }
        model.addAttribute("error", "User not found");
        return "security/login";
    }
    @PostMapping
    public String completeProfile(@ModelAttribute("profileDTO") ProfileDTO profileDTO,
                                  Model model) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user != null && !user.is_complete_profile()) {
                String message = completeProfileService.completeProfile(user, profileDTO);
                model.addAttribute("success", message);
                return redirectToRoleBasedHome(user);
            }
        }
        return "security/complete-profile";
    }

    private String redirectToRoleBasedHome(User user) {
        switch (user.getRole()) {
            case CAR_OWNER:
                return "redirect:/rent-car/owner/home";
            case CUSTOMER:
                return "redirect:/rent-car/customer/home";
            default:
                return "redirect:/rent-car/customer/home";
        }
    }
}
