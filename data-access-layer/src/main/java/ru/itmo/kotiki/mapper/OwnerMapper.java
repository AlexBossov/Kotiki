package ru.itmo.kotiki.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.entity.Owner;

@Component
public class OwnerMapper {
    public final UserMapper userMapper;

    @Autowired
    public OwnerMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public OwnerDTO convertOwnerToOwnerDTO(Owner owner) {
        if (owner == null) {
            return null;
        }
        return new OwnerDTO(owner.getId(),
                owner.getName(),
                owner.getBirthday(),
                userMapper.convertUserToUserDTO(owner.getUser()));
    }

    public Owner convertOwnerDTOToOwner(OwnerDTO ownerDTO) {
        if (ownerDTO == null) {
            return null;
        }
        return new Owner(ownerDTO.getId(),
                ownerDTO.getName(),
                ownerDTO.getBirthday(),
                userMapper.convertUserDTOToUser(ownerDTO.getUserDTO()));
    }
}
