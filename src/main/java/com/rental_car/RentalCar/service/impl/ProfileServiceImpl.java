package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.dto.ProfileDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.ProfileMapper;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.CustomerRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Car_OwnerRepository carOwnerRepository;

    @Override
    public void updateProfile(User user, ProfileDTO profileDTO){
        user.setEmail(profileDTO.getEmail());
        userRepository.save(user);
        if (user.getRole().equals(User.Role.CUSTOMER)){
            Customer customer = user.getCustomer();
            customer.setName(profileDTO.getName());
            customer.setDate_of_birth(profileDTO.getDate_of_birth());
            customer.setPhone_no(profileDTO.getPhone_no());
            customer.setEmail(profileDTO.getEmail());
            customer.setNational_id_no(profileDTO.getNational_id_no());
            customer.setAddress(profileDTO.getAddress());
            customer.setDriving_license(profileDTO.getDriving_license());
            customerRepository.save(customer);
        } else if (user.getRole().equals(User.Role.CAR_OWNER)){
            Car_Owner carOwner = user.getCarOwner();
            carOwner.setName(profileDTO.getName());
            carOwner.setDate_of_birth(profileDTO.getDate_of_birth());
            carOwner.setPhone_no(profileDTO.getPhone_no());
            carOwner.setEmail(profileDTO.getEmail());
            carOwner.setNational_id_no(profileDTO.getNational_id_no());
            carOwner.setAddress(profileDTO.getAddress());
            carOwner.setDriving_license(profileDTO.getDriving_license());
            carOwnerRepository.save(carOwner);
        }
    }

}

