package com.solactive.realtimestatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealtimeStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeStatisticsApplication.class, args);
    }

}
