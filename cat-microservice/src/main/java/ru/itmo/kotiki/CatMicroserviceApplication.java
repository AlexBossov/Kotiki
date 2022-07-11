package ru.itmo.kotiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CatMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatMicroserviceApplication.class, args);
    }
}