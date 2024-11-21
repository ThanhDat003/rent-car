package com.rental_car.RentalCar.controller.customer;

import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.CustomerRepository;
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
@RequestMapping("/rent-car/customer/wallet")
public class WalletController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showWallet(Model model) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        if(user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            String name = carOwner.getName();
            model.addAttribute("name", name);
        } else if (user.getRole().equals(User.Role.CUSTOMER)) {
            Customer customer = user.getCustomer();
            String name = customer.getName();
            model.addAttribute("name", name);
        }
        //String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Customer customer = customerRepository.findByEmail(email);
        Long walletBalance = walletService.getWalletBalance(customer);
        model.addAttribute("walletBalance", walletBalance);
        return "customer/wallet";
    }

    @PostMapping("/add-funds")
    public String addFunds(@RequestParam("amount") Long amount, RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Customer customer = customerRepository.findByEmail(email);
        try {
            walletService.addFunds(customer, amount);
            redirectAttributes.addFlashAttribute("success", "Funds added successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rent-car/customer/wallet";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("amount") Long amount, RedirectAttributes redirectAttributes) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Customer customer = customerRepository.findByEmail(email);
        try {
            walletService.deductFunds(customer, amount);
            redirectAttributes.addFlashAttribute("success", "Funds withdrawn successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rent-car/customer/wallet";
    }
}
