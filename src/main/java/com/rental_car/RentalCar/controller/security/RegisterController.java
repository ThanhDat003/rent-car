package com.rental_car.RentalCar.controller.security;

import com.rental_car.RentalCar.entity.Token;
import com.rental_car.RentalCar.repository.TokenRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ActivateAccount;
import com.rental_car.RentalCar.service.RegisterService;
import com.rental_car.RentalCar.util.Validate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/rent-car/signup")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Validate validate;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private ActivateAccount activateAccount;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegisterService registerService;

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam("signupEmail") String email) {
        System.out.println("Check email availability: " + email);
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    @GetMapping
    public String register() {
        return "security/login";
    }
    @PostMapping
    public String processRegister(@RequestParam("signupName") String name,
                                  @RequestParam("signupEmail") String email,
                                  @RequestParam("signupPassword") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  @RequestParam("signupPhone") String phone,
                                  @RequestParam("role") String role,
                                  Model model) {
        System.out.println("Hello");
        if(userRepository.existsByEmail(email)) {
            model.addAttribute("signupName", name);
            model.addAttribute("signupPhone", phone);
            model.addAttribute("errorSignup", "Please use another email!");
            return "security/login";
        } else {
            if(!validate.validateFullName(name)) {
                model.addAttribute("errorName", "Full name must be in the format 'First Last' (Ex: John Doe)");
                return "security/login";
            }else if (!validate.validateEmail(email)) {
                model.addAttribute("errorEmail", "Please enter a valid email address");
                return "security/login";
            }else if (!validate.validatePassword(password)) {
                model.addAttribute("errorPassword", "Password must be at least 7 characters long and contain at least one letter and one number");
                return "security/login";
            }else if (!validate.validatePhone(phone)) {
                model.addAttribute("errorPhone", "Please enter a valid phone number");
                return "security/login";
            }else if (!password.equals(confirmPassword)) {
                model.addAttribute("errorConfirmPassword", "Password and Confirm Password do not match");
                return "security/login";
            }else {
                Token token = new Token();
                token.setEmail(email);
                token.setName(name);
                token.setPhone(phone);
                token.setRole(role);
                if (tokenRepository.findByEmail(email) != null) {
                    model.addAttribute("errorSignup", "Please check your email to activate your account");
                    return "security/login";
                } else if (tokenRepository.findByEmail(email) != null && token.getExpiredDate().isBefore(LocalDateTime.now())) {
                    tokenRepository.delete(tokenRepository.findByEmail(email));
                    model.addAttribute("errorSignup", "Token expired please try again to update your token");
                    return "security/login";
                }
                tokenRepository.save(token);
                System.out.println("email: " + email);
                try{
                    activateAccount.activateAccount(email, passwordEncoder.encode(password));
                    model.addAttribute("success", "Activation link sent to " + email + "\nPlease check your email to activate your account");
                } catch (MessagingException e) {
                    model.addAttribute("errorSignup", "Error while sending activation email");
                    throw new RuntimeException(e);
                }
                return "security/login";
            }
        }
    }

    @GetMapping("/activate")
    public String activateRequest(@RequestParam("token") String token,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  Model model) {
        System.out.println("Check nè");
        System.out.println(token + " " + email + " " + password);
        Token tokencheck = tokenRepository.findByEmail(email);
        if (tokencheck == null) {
            model.addAttribute("errorSignup", "Email not found");
            return "security/login";
        }else if(tokencheck.getToken().equals(token)) {
            registerService.registerAccount(email, password, tokencheck.getName(), tokencheck.getPhone(), tokencheck.getRole());
            tokenRepository.delete(tokenRepository.findByEmail(email));
            model.addAttribute("success", "Account activated successfully");
            return "security/login";
        } else {
            model.addAttribute("errorSignup", "Invalid token");
            return "security/login";
        }
    }
}
