package com.rental_car.RentalCar.security;

import com.rental_car.RentalCar.dto.LoginDTO;
import com.rental_car.RentalCar.entity.User;
import com.rental_car.RentalCar.mapper.LoginMapper;
import com.rental_car.RentalCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            LoginDTO loginDTO = loginMapper.toLoginDTO(user);
            var springUser = org.springframework.security.core.userdetails.User.withUsername(loginDTO.getEmail())
                    .password(loginDTO.getPassword())
                    .authorities(mapRolesToAuthorities(Collections.singleton(String.valueOf(user.getRole()))))
                    .build();
            return springUser;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
