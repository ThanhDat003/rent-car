package com.rental_car.RentalCar.controller;

import com.rental_car.RentalCar.dto.FeedbackDTO;
import com.rental_car.RentalCar.entity.Feedback;
import com.rental_car.RentalCar.mapper.FeedbackMapper;
import com.rental_car.RentalCar.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rent-car")
public class GuestController {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @GetMapping(value = {"/", "/home", ""})
    public String home(Model model) {
        List<FeedbackDTO> feedback = feedbackRepository.findAll().stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("feedbacks", feedback);
        return "guest-home";
    }
}
