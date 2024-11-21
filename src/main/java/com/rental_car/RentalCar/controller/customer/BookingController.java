package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.CarMapper;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.BookingService;
import com.rental_car.RentalCar.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rent-car/customer")
public class BookingController {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/rent/carId/{id}")
    public String booking(@RequestParam("name") String name,
                          @RequestParam("start_date_time") LocalDateTime start_date_time,
                          @RequestParam("end_date_time") LocalDateTime end_date_time,
                          @PathVariable("id") Long id,
                          Model model) {

        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name1 = carOwner.getName();
            model.addAttribute("name", name1);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name1 = customer.getName();
            model.addAttribute("name", name1);
        }

        addCommonAttributes(model, name, start_date_time, end_date_time, id);

        return "customer/rent-car/booking-info";
    }

    @GetMapping("/rent/carId/{id}/payment")
    public String payment(@RequestParam("name") String name,
                          @RequestParam("start_date_time") LocalDateTime start_date_time,
                          @RequestParam("end_date_time") LocalDateTime end_date_time,
                          @PathVariable("id") Long id,
                          Model model){
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name1 = carOwner.getName();
            model.addAttribute("name", name1);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name1 = customer.getName();
            model.addAttribute("name", name1);
        }

        addCommonAttributes(model, name, start_date_time, end_date_time, id);

        return "customer/rent-car/booking-payment";
    }

    private void addCommonAttributes(Model model,
                                         @RequestParam("name") String name,
                                         @RequestParam("start_date_time") LocalDateTime start_date_time,
                                         @RequestParam("end_date_time") LocalDateTime end_date_time,
                                         @PathVariable("id") Long id) {
        Car car = carRepository.findById(id).orElseThrow();
        CarDTO carDTO = carMapper.toDTO(car);
        double rating = ratingService.calculateRating(id);
        List<String> images = Arrays.asList(carDTO.getImages().split(","));
        int days = bookingService.calculateDays(start_date_time, end_date_time);
        Long totalPrice = bookingService.calculateTotalPrice(start_date_time, end_date_time, String.valueOf(car.getBase_price()));

        model.addAttribute("name1", name);
        model.addAttribute("start_date_time", start_date_time);
        model.addAttribute("end_date_time", end_date_time);
        model.addAttribute("car", carDTO);
        model.addAttribute("images", images);
        model.addAttribute("rating", rating);
        model.addAttribute("days", days);
        model.addAttribute("totalPrice", totalPrice);
    }
}
