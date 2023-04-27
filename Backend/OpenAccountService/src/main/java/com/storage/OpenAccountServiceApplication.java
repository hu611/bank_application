package com.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OpenAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenAccountServiceApplication.class, args);
    }

}
