package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.CarMapper;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.CarDetailService;
import com.rental_car.RentalCar.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rent-car/customer")
public class CarDetailController {
    @Autowired
    private CarDetailService carDetailService;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/car/detail/carId={id}")
    public String carDetail(Model model,
                            @PathVariable("id") Long id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name1 = carOwner.getName();
            model.addAttribute("name1", name1);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name1 = customer.getName();
            model.addAttribute("name1", name1);
        }
        Car car = carDetailService.getCarDetail(id);
        CarDTO carDTO = carMapper.toDTO(car);
        double rating = ratingService.calculateRating(id);
        List<String> images = Arrays.asList(carDTO.getImages().split(","));
        System.out.println(images);
        model.addAttribute("car", carDTO);
        model.addAttribute("images", images);
        model.addAttribute("rating", rating);
        return "customer/car-details";
    }
}
