package ru.itmo.kotiki.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itmo.kotiki.enums.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatDTO {
    private final Long id;
    private final String name;
    private final LocalDate birthday;
    private final Color color;
    private final String breed;
    private final OwnerDTO owner;
    private final List<CatDTO> cats;

    @JsonCreator
    public CatDTO(@JsonProperty("id") Long id,
                  @JsonProperty("name") String name,
                  @JsonProperty("birthday") LocalDate birthday,
                  @JsonProperty("color") Color color,
                  @JsonProperty("breed") String breed,
                  @JsonProperty("owner") OwnerDTO owner,
                  @JsonProperty("cats") List<CatDTO> cats) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.color = color;
        this.breed = breed;
        this.owner = owner;
        this.cats = Objects.requireNonNullElseGet(cats, ArrayList::new);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Color getColor() {
        return color;
    }

    public String getBreed() {
        return breed;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public List<CatDTO> getCats() {
        return cats;
    }
}
