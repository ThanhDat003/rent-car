package com.rental_car.RentalCar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rent-car")
public class GuestController {

    @GetMapping(value = {"/", "/home", ""})
    public String home() {
        return "guest-home";
    }
}
