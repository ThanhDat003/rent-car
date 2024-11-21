package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.entity.*;
import com.rental_car.RentalCar.repository.BookingRepository;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ListBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rent-car/customer")
public class ListBookingController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ListBookingService listBookingService;

    @GetMapping("/list-booking")
    public String listBooking(Model model) {
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

        List<Booking> bookings = bookingRepository.findAllByCustomer(user.getCustomer());

        Map<Long, Integer> bookingDays = new HashMap<>();
        for (Booking booking : bookings) {
            int days = (int) Duration.between(booking.getStartDateTime(), booking.getEndDateTime()).toDays();
            bookingDays.put(booking.getId(), days);
        }
        Map<Long, List<String>> imagesMap = bookings.stream().collect(Collectors.toMap(
                booking -> booking.getId(),
                booking -> List.of(booking.getCar().getImages().split(","))
        ));

        model.addAttribute("bookings", bookings);
        model.addAttribute("images", imagesMap);
        model.addAttribute("bookingDays", bookingDays);

        return "customer/my-booking-list";
    }
}
