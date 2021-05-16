package com.example.coronavirustracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // Enables scheduling for our App
public class CoronavirusTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronavirusTrackerApplication.class, args);
    }

}
