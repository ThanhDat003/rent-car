package com.rental_car.RentalCar.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinaryConfiguration() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dwabdbbfj");
        config.put("api_key", "597347638282519");
        config.put("api_secret", "TaPIFZb2O6ry3inO-4sjWYVzW8w");
        config.put("secure", "true");
        return new Cloudinary(config);
    }
}
