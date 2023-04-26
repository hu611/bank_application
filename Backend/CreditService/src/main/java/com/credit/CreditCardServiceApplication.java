package com.credit;

import com.credit.Service.CronSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = "com.credit.Service.feign")
@SpringBootApplication
public class CreditCardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditCardServiceApplication.class, args);
    }

}
