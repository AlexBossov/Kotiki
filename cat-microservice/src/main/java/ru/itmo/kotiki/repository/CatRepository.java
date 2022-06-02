package ru.itmo.kotiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.kotiki.entity.Cat;
import ru.itmo.kotiki.enums.Color;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findCatByOwner_User_Username(String username);

    List<Cat> findCatByBreedAndOwner_User_Username(String breed, String username);

    List<Cat> findCatByColorAndOwner_User_Username(Color color, String username);
}
