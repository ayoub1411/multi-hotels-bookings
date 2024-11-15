package com.booking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookingApplication {


    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }


    @Bean
    CommandLineRunner runner() {

        return args -> {


        };
    }
}
