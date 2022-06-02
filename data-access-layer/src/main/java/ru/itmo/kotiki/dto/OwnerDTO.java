package ru.itmo.kotiki.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class OwnerDTO {
    private final Long id;
    private final String name;
    private final LocalDate birthday;
    private final UserDTO userDTO;


    @JsonCreator
    public OwnerDTO(@JsonProperty("id") Long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("birthday") LocalDate birthday,
                    @JsonProperty("user") UserDTO userDTO) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.userDTO = userDTO;
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

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
