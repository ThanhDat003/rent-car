package com.rental_car.RentalCar.security;

import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        String redirectUrl = request.getContextPath();
        if (user != null && !user.is_complete_profile()){
            redirectUrl = "/rent-car/complete-profile";
        }else {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_CAR_OWNER")) {
                    redirectUrl = "/rent-car/owner/home";
                    break;
                } else if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                    redirectUrl = "/rent-car/customer/home";
                    break;
                }
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
