package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.dto.FeedbackDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.FeedbackMapper;
import com.rental_car.RentalCar.repository.FeedbackRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rent-car/customer/home")
public class HomeCustomerController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackMapper feedbackMapper;
    @GetMapping
    public String homeCustomer(Model model) {
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
        List<FeedbackDTO> feedback = feedbackRepository.findAll().stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("feedbacks", feedback);
        return "customer/customer-home";
    }
}
