package com.byteasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class BootStrapApplication {

    static void main(String[] args) {
        SpringApplication.run(BootStrapApplication.class, args);
    }

}

