package com.rental_car.RentalCar.controller.carowner;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.*;
import com.rental_car.RentalCar.mapper.CarMapper;
import com.rental_car.RentalCar.repository.BookingRepository;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rent-car/owner/list-car")
public class ListCarController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping()
    public String showListCar(Model model) {
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

        try {
            List<Car> cars = carRepository.findByCarOwnerOrderByIdDesc(user.getCarOwner());
            List<CarDTO> carDTOS = cars.stream()
                    .map(carMapper::toDTO)
                    .collect(Collectors.toList());
            Map<Long, Double> carRatings = cars.stream()
                    .collect(Collectors.toMap(Car::getId, car -> ratingService.calculateRating(car.getId())));
            Map<Long, List<String>> carImages = carDTOS.stream()
                    .collect(Collectors.toMap(CarDTO::getId, car -> List.of(car.getImages().split(","))));
            Map<Long, String> carBookingStatuses = cars.stream()
                    .collect(Collectors.toMap(
                            Car::getId,
                            car -> {
                                List<Booking> bookings = bookingRepository.findByCarId(car.getId());
                                return bookings.isEmpty() ? "Available" : bookings.get(0).getStatus();
                            }
                    ));

            model.addAttribute("cars", carDTOS);
            model.addAttribute("carRatings", carRatings);
            model.addAttribute("carImages", carImages);
            model.addAttribute("carBookingStatuses", carBookingStatuses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "car-owner/list-car";
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(@RequestParam("carId") Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        List<Booking> bookings = bookingRepository.findByCarId(carId);
        if (!bookings.isEmpty()) {
            Booking booking = bookings.get(0);
            booking.setStatus("Booked");
            car.setStatus("In Use");
            bookingRepository.save(booking);
            carRepository.save(car);
        }
        return "redirect:/rent-car/owner/list-car";
    }
}
