package com.food.table;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodTableApplication.class, args);
    }

}
