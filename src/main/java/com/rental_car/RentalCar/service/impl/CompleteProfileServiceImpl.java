package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.CustomerRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.CompleteProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompleteProfileServiceImpl implements CompleteProfileService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Car_OwnerRepository carOwnerRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String completeProfile(User user, ProfileDTO profileDTO) {
        if (user == null) {
            System.out.println("Fail r");
            return "User not found";
        } else {
            if (user.getRole().equals(User.Role.CUSTOMER)) {
                Customer customer = user.getCustomer() != null ? user.getCustomer() : new Customer();
                customer.setDate_of_birth(profileDTO.getDate_of_birth());
                customer.setNational_id_no(profileDTO.getNational_id_no());
                customer.setAddress(profileDTO.getAddress());
                customer.setDriving_license(profileDTO.getDriving_license());

                customerRepository.save(customer);
                user.setCustomer(customer);

            } else if (user.getRole().equals(User.Role.CAR_OWNER)) {
                Car_Owner carOwner = user.getCarOwner() != null ? user.getCarOwner() : new Car_Owner();
                carOwner.setDate_of_birth(profileDTO.getDate_of_birth());
                carOwner.setNational_id_no(profileDTO.getNational_id_no());
                carOwner.setAddress(profileDTO.getAddress());
                carOwner.setDriving_license(profileDTO.getDriving_license());

                carOwnerRepository.save(carOwner);
                user.setCarOwner(carOwner);
            }
        }

        user.set_complete_profile(true);
        userRepository.save(user);

        return "Profile completed successfully!";
    }
}