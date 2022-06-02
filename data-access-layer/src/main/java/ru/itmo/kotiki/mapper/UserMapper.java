package ru.itmo.kotiki.mapper;

import org.springframework.stereotype.Component;
import ru.itmo.kotiki.dto.RoleDTO;
import ru.itmo.kotiki.dto.UserDTO;
import ru.itmo.kotiki.entity.Role;
import ru.itmo.kotiki.entity.User;

@Component
public class UserMapper {
    public UserDTO convertUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(),
                user.getRoles().stream().map(r -> new RoleDTO(r.getId(), r.getName())).toList());
    }

    public User convertUserDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getRoles().stream().map(r -> new Role(r.getId(), r.getName())).toList());
    }
}
