package ru.itmo.kotiki.service;

import ru.itmo.kotiki.dto.RoleDTO;
import ru.itmo.kotiki.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO user);

    RoleDTO saveRole(RoleDTO role);

    void addRoleTOUser(String username, String roleName);

    UserDTO getUser(String username);

    List<UserDTO> getUsers();
}
