package com.rental_car.RentalCar.service.impl;

import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;
import com.rental_car.RentalCar.repository.Car_OwnerRepository;
import com.rental_car.RentalCar.repository.CustomerRepository;
import com.rental_car.RentalCar.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Car_OwnerRepository carOwnerRepository;

    @Override
    public Long getWalletBalance(Customer customer) {
        return customer.getWallet();
    }

    @Override
    public void addFunds(Customer customer, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        customer.setWallet(customer.getWallet() + amount);
        customerRepository.save(customer);
    }

    @Override
    public void deductFunds(Customer customer, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (customer.getWallet() < amount) {
            throw new IllegalArgumentException("Insufficient funds in wallet.");
        }
        customer.setWallet(customer.getWallet() - amount);
        customerRepository.save(customer);
    }

    @Override
    public Long getWalletBalanceOwner(Car_Owner carOwner) {
        return carOwner.getWallet();
    }

    @Override
    public void addFundsOwner(Car_Owner carOwner, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        carOwner.setWallet(carOwner.getWallet() + amount);
        carOwnerRepository.save(carOwner);
    }

    @Override
    public void deductFundsOwner(Car_Owner carOwner, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (carOwner.getWallet() < amount) {
            throw new IllegalArgumentException("Insufficient funds in wallet.");
        }
        carOwner.setWallet(carOwner.getWallet() - amount);
        carOwnerRepository.save(carOwner);
    }
}
