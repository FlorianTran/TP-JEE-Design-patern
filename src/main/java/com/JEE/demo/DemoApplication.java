package com.JEE.demo;

import com.JEE.demo.service.LoanService;
import com.JEE.demo.service.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initObservers(LoanService loanService, NotificationService notif) {
        return args -> loanService.addObserver(notif);
    }
}
