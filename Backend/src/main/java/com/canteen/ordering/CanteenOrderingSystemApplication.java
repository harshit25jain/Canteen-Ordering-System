package com.canteen.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CanteenOrderingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanteenOrderingSystemApplication.class, args);
    }

}

