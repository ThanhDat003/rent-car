package com.rental_car.RentalCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ratings")
    private int ratings;
    @Column(name = "content")
    private String content;
    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;
}
