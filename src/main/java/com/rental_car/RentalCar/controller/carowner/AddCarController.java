package com.rental_car.RentalCar.controller.carowner;

import com.rental_car.RentalCar.dto.CarDTO;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.CarMapper;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.AddCarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rent-car/owner/add-car")
public class AddCarController {

    @Autowired
    private AddCarService addCarService;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/step-1")
    public String step1(Model model) {
        model.addAttribute("car", new CarDTO());
        return "car-owner/add-cart-1";
    }

    @PostMapping("/step-1")
    public String step1(@ModelAttribute("car") CarDTO carDTO,
                        HttpSession session) {
        session.setAttribute("car", carDTO);
        return "redirect:/rent-car/owner/add-car/step-2";
    }

    @GetMapping("/step-2")
    public String step2(Model model,
                        HttpSession session) {
        CarDTO carDTO = (CarDTO) session.getAttribute("car");
        model.addAttribute("car", carDTO);
        return "car-owner/add-cart-2";
    }

    @PostMapping("/step-2")
    public String step2(@RequestParam("images") List<MultipartFile> files,
                        @RequestParam("mileage") Double mileage,
                        @RequestParam("fuel") String fuel,
                        @RequestParam("address") String address,
                        @RequestParam("description") String description,
                        @RequestParam("additional_functions") String additional_functions,
                        HttpSession session) {
        try {
            CarDTO carDTO = (CarDTO) session.getAttribute("car");

            if (carDTO == null) {
                carDTO = new CarDTO();
                session.setAttribute("car", carDTO);
                //throw new IllegalStateException("CarDTO not found in session.");
            }

            List<String> uploadedUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String uploadedUrl = addCarService.uploadImage(file);
                    uploadedUrls.add(uploadedUrl);
                }
            }

            String imagesAsString = String.join(",", uploadedUrls);
            carDTO.setImages(imagesAsString);
            System.out.println("length: " + carDTO.getImages().length());

            carDTO.setMileage(mileage);
            carDTO.setFuel_consumption(Double.parseDouble(fuel));
            carDTO.setAddress(address);
            carDTO.setDescription(description);
            carDTO.setAdditional_functions(additional_functions);
            session.setAttribute("car", carDTO);

            return "redirect:/rent-car/owner/add-car/step-3";
        } catch (Exception e) {
            e.printStackTrace();
            return "error-page";
        }
    }



    @GetMapping("/step-3")
    public String step3(Model model,
                        HttpSession session) {
        CarDTO carDTO = (CarDTO) session.getAttribute("car");
        model.addAttribute("car", carDTO);
        return "car-owner/add-cart-3";
    }

    @PostMapping("/step-3")
    public String step3(HttpSession session,
                        @RequestParam("basePrice") String basePrice,
                        @RequestParam("deposit") String deposit,
                        @RequestParam("terms_of_use") String termsOfUse) {
        System.out.println("Base Price: " + basePrice);
        System.out.println("Deposit: " + deposit);
        System.out.println("Terms: " + termsOfUse);
        CarDTO carDTO = (CarDTO) session.getAttribute("car");
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        try {
            carDTO.setName(carDTO.getBrand() + " " + carDTO.getModel());
            carDTO.setCarOwner(user.getCarOwner());
            carDTO.setBase_price(Long.parseLong(basePrice));
            carDTO.setDeposit(Double.parseDouble(deposit));
            carDTO.setTerms_of_use(termsOfUse);
            carDTO.setStatus("Available");
            addCarService.addCar(carMapper.toEntity(carDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/rent-car/owner/add-car/step-4";
    }

    @GetMapping("/step-4")
    public String step4() {
        return "car-owner/add-cart-4";
    }
//
//    @PostMapping("/step-4")
//    public String saveAndFinish(@ModelAttribute("car") CarDTO carDTO,
//                                HttpSession session) {
//        addCarService.addCar(carMapper.toEntity(carDTO));
//        session.removeAttribute("car");
//        return "redirect:/rent-car/owner/home";
//    }
}
