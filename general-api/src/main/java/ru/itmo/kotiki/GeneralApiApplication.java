package ru.itmo.kotiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itmo.kotiki.dto.RoleDTO;
import ru.itmo.kotiki.dto.UserDTO;
import ru.itmo.kotiki.service.UserService;

import java.util.ArrayList;

@SpringBootApplication
public class GeneralApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneralApiApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole(new RoleDTO(null, "ROLE_USER"));
//            userService.saveRole(new RoleDTO(null, "ROLE_ADMIN"));
//
//            userService.saveUser(new UserDTO(null, "john", "1234", new ArrayList<>()));
//            userService.saveUser(new UserDTO(null, "alexbossov", "test", new ArrayList<>()));
//            userService.saveUser(new UserDTO(null, "kira", "1234test", new ArrayList<>()));
//            userService.saveUser(new UserDTO(null, "kek", "rrrttt", new ArrayList<>()));
//
//            userService.addRoleTOUser("john", "ROLE_ADMIN");
//            userService.addRoleTOUser("john", "ROLE_USER");
//            userService.addRoleTOUser("alexbossov", "ROLE_USER");
//            userService.addRoleTOUser("kira", "ROLE_USER");
//            userService.addRoleTOUser("kek", "ROLE_USER");
//        };
//    }
}
