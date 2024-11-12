package com.rental_car.RentalCar.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("rent-car/login")
public class LoginController {
    @GetMapping
    public String login() {
        return "security/login";
    }
}
