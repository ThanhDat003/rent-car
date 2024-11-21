package com.rental_car.RentalCar.service;

import com.rental_car.RentalCar.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AddCarService {
    Car addCar(Car car);
    String uploadImage(MultipartFile file) throws Exception;

    String uploadImages(MultipartFile[] files) throws IOException;
}
