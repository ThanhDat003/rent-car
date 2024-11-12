package com.rental_car.RentalCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @Column(name = "email", length = 30, unique = true, nullable = false)
    private String email;
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "car_owner_id")
    private Car_Owner carOwner;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    public enum Role {
        CAR_OWNER, CUSTOMER
    }

    private boolean is_complete_profile;
}
