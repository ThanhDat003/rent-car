package com.rental_car.RentalCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, unique = true)
    private String token;
    @Column(nullable = true)
    private String email;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User user;
    @Column(nullable = true)
    private LocalDateTime expiredDate;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String phone;
    @Column(nullable = true)
    private String role;

}
