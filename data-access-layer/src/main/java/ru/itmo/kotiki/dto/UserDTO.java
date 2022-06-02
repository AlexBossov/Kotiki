package ru.itmo.kotiki.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDTO {
    private final Long id;
    private final String username;
    @JsonIgnore
    private final String password;
    private final List<RoleDTO> roles;

    public UserDTO(@JsonProperty("id") Long id,
                   @JsonProperty("username") String username,
                   @JsonProperty("password") String password,
                   @JsonProperty("roles") List<RoleDTO> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
