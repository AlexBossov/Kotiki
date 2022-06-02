package ru.itmo.kotiki.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleDTO {
    private final Long id;
    private final String name;

    public RoleDTO(@JsonProperty("id") Long id,
                   @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
