package com.rental_car.RentalCar.controller.carowner;

import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("rent-car/owner")
public class CarOwnerController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String carOwner(Model model) {
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
        return "car-owner/car-owner-home";
    }
}
