package ru.itmo.kotiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.kotiki.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
