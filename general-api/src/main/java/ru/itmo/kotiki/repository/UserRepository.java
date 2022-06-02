package ru.itmo.kotiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.kotiki.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}