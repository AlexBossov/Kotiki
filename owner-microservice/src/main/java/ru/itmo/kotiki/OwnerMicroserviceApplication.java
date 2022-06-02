package ru.itmo.kotiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.mapper.OwnerMapper;
import ru.itmo.kotiki.mapper.UserMapper;

@EnableKafka
@SpringBootApplication
public class OwnerMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnerMicroserviceApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
