package com.rental_car.RentalCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 30)
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate date_of_birth;
    @Column(name = "national_id_no", length = 15)
    private String national_id_no;
    @Column(name = "phone_no", length = 15)
    private String phone_no;
    @Column(name = "email", length = 30, unique = true)
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "driving_license", length = 50)
    private String driving_license;
    @Column(name = "wallet")
    private Long wallet;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
