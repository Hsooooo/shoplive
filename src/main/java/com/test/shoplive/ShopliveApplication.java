package com.test.shoplive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ShopliveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopliveApplication.class, args);
    }

}
