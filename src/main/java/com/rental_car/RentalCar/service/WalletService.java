package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.entity.Car_Owner;
import com.rental_car.RentalCar.entity.Customer;

public interface WalletService {
    Long getWalletBalance(Customer customer);
    void addFunds(Customer customer, Long amount);
    void deductFunds(Customer customer, Long amount);

    Long getWalletBalanceOwner(Car_Owner carOwner);
    void addFundsOwner(Car_Owner carOwner, Long amount);
    void deductFundsOwner(Car_Owner carOwner, Long amount);
}
