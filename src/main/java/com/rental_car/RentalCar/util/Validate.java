package com.rental_car.RentalCar.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validate {

    public boolean validateFullName(String fullname) {
        Pattern pattern = Pattern.compile("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$");
        return pattern.matcher(fullname).matches();
    }

    public boolean validateEmail(String email) {
        Pattern pattern1 = Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return pattern1.matcher(email).matches();
    }

    public boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$");
        return pattern.matcher(password).matches();
    }

    public boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        return pattern.matcher(phone).matches();
    }

}