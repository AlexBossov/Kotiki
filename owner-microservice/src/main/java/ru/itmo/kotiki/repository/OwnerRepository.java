package ru.itmo.kotiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.kotiki.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}