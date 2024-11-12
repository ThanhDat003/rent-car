package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.dto.Car_OwnerDTO;
import com.rental_car.RentalCar.dto.CustomerDTO;
import com.rental_car.RentalCar.dto.Role;
import com.rental_car.RentalCar.dto.UserDTO;
import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.Car_OwnerMapper;
import com.rental_car.RentalCar.mapper.CustomerMapper;
import com.rental_car.RentalCar.mapper.UserMapper;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.CustomerRepository;
import com.rental_car.RentalCar.repository.UserRepository;
import com.rental_car.RentalCar.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Car_OwnerMapper car_ownerMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Car_OwnerRepository car_ownerRepository;

    @Override
    public void registerAccount(String email, String password, String name, String phone, String role) {
        if (role.equals("CUSTOMER")){
            CustomerDTO customer = new CustomerDTO();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhone_no(phone);
            Customer customerEntity = customerMapper.toEntity(customer);
            customerRepository.save(customerEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setRole(Role.CUSTOMER);
            userDTO.set_complete_profile(false);
            User userEntity = userMapper.toEntity(userDTO);
            userEntity.setCustomer(customerEntity);
            userRepository.save(userEntity);

        }else if (role.equals("CAR_OWNER")){
            Car_OwnerDTO car_owner = new Car_OwnerDTO();
            car_owner.setName(name);
            car_owner.setEmail(email);
            car_owner.setPhone_no(phone);
            Car_Owner car_ownerEntity = car_ownerMapper.toEntity(car_owner);
            car_ownerRepository.save(car_ownerEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setRole(Role.CAR_OWNER);
            userDTO.set_complete_profile(false);
            User userEntity = userMapper.toEntity(userDTO);
            userEntity.setCarOwner(car_ownerEntity);
            userRepository.save(userEntity);
        }
    }
}
