package com.rental_car.RentalCar.config;

import com.rental_car.RentalCar.security.CustomAuthenticationSuccessHandler;
import com.rental_car.RentalCar.security.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/rent-car/", "/rent-car/home", "/rent-car").permitAll()
                .requestMatchers("/rent-car/login").permitAll()
                .requestMatchers("rent-car/signup").permitAll()
                .requestMatchers("/rent-car/signup/check-email").permitAll()
                .requestMatchers("/rent-car/signup/activate").permitAll()
                .requestMatchers("/rent-car/forgot-password").permitAll()
                .requestMatchers("/rent-car/reset-password").permitAll()
                .requestMatchers("/rent-car/owner/**").hasRole("CAR_OWNER")
                .requestMatchers("/rent-car/customer/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .formLogin(formLogin -> formLogin
                        .loginPage("/rent-car/login")
                        .loginProcessingUrl("/rent-car/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/rent-car/logout"))
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/rent-car/guest")
                        .permitAll());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
