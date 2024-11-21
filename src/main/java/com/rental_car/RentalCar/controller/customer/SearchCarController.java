package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.CarMapper;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.RatingService;
import com.rental_car.RentalCar.service.SearchCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rent-car/customer")
public class SearchCarController {
    @Autowired
    private SearchCarService searchCarService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarMapper carMapper;

    @GetMapping("/search")
    public String searchCar(@RequestParam("name") String name,
                            @RequestParam("start_date_time") LocalDateTime start_date_time,
                            @RequestParam("end_date_time") LocalDateTime end_date_time,
                            Model model) {
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
        try{
            List<Car> cars = searchCarService.searchCarAvailable(name, start_date_time, end_date_time);
            List<CarDTO> carDTOS = cars.stream()
                    .map(carMapper::toDTO)
                    .collect(Collectors.toList());
            Map<Long, Double> carRatings = cars.stream()
                            .collect(Collectors.toMap(Car::getId, car -> ratingService.calculateRating(car.getId())));
            Map<Long, List<String>> carImages = carDTOS.stream()
                            .collect(Collectors.toMap(CarDTO::getId, car -> List.of(car.getImages().split(","))));
            System.out.println(carImages);
            model.addAttribute("cars", carDTOS);
            model.addAttribute("carRatings", carRatings);
            model.addAttribute("carImages", carImages);
            model.addAttribute("name", name);
            model.addAttribute("start_date_time", start_date_time);
            model.addAttribute("end_date_time", end_date_time);
            return "customer/search-car-result";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/404";
        }
    }
}
