package com.rental_car.RentalCar.mapper;

import com.rental_car.RentalCar.dto.FeedbackDTO;
import com.rental_car.RentalCar.entity.Feedback;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public FeedbackMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public FeedbackDTO toDTO(Feedback feedback) {
        return modelMapper.map(feedback, FeedbackDTO.class);
    }

    public Feedback toEntity(FeedbackDTO feedbackDTO) {
        return modelMapper.map(feedbackDTO, Feedback.class);
    }
}
