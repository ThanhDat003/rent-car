package com.rental_car.RentalCar.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rental_car.RentalCar.entity.Car;
import com.rental_car.RentalCar.repository.CarRepository;
import com.rental_car.RentalCar.service.AddCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddCarServiceImpl implements AddCarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    @Override
    public String uploadImages(MultipartFile[] files) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadImage(file);
            imageUrls.add(url);
        }
        return String.join(",", imageUrls);
    }
}
