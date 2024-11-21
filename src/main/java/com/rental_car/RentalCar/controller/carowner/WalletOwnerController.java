package com.rental_car.RentalCar.controller.carowner;

import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rent-car/owner/wallet")
public class WalletOwnerController {

    @Autowired
    private Car_OwnerRepository carOwnerRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showWallet(Model model) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        Car_Owner carOwner = user.getCarOwner();
        Long walletBalance = walletService.getWalletBalanceOwner(carOwner);
        model.addAttribute("walletBalance", walletBalance);
        model.addAttribute("name", carOwner.getName());
        return "car-owner/wallet";
    }

    @PostMapping("/add-funds")
    public String addFunds(@RequestParam("amount") Long amount, RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        Car_Owner carOwner = user.getCarOwner();
        try {
            walletService.addFundsOwner(carOwner, amount);
            redirectAttributes.addFlashAttribute("success", "Funds added successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rent-car/owner/wallet";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("amount") Long amount, RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        Car_Owner carOwner = user.getCarOwner();
        try {
            walletService.deductFundsOwner(carOwner, amount);
            redirectAttributes.addFlashAttribute("success", "Funds withdrawn successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rent-car/owner/wallet";
    }
}
