package com.booking;

import com.booking.dao.UserRepository;
import com.booking.entities.Admin;
import com.booking.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
@EnableAsync
public class BookingApplication {

    @Autowired
    UserRepository repository;
    @Autowired
    PasswordEncoder encoder;
    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }




    @Bean
    CommandLineRunner runner(){

        return args -> {




        };
    }
}
