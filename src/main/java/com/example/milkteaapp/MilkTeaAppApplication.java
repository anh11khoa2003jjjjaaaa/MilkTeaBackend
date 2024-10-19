package com.example.milkteaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan("com.example.milkteaapp.Model")
@SpringBootApplication
public class MilkTeaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilkTeaAppApplication.class, args);
    }

}
