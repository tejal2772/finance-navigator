package com.financenavigator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.financenavigator"})
@EntityScan({"com.financenavigator.entity"})
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        System.out.println("Finance Navigator Service");
    }
}