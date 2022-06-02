package ru.itmo.kotiki.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.kotiki.dto.CatDTO;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.entity.Cat;
import ru.itmo.kotiki.entity.Owner;

@Component
public class CatMapper {

    public final UserMapper userMapper;

    @Autowired
    public CatMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CatDTO convertCatToCatDTO(Cat cat) {
        if (cat == null) {
            return null;
        }
        return new CatDTO(cat.getId(),
                cat.getName(),
                cat.getBirthday(),
                cat.getColor(),
                cat.getBreed(),
                new OwnerDTO(cat.getOwner().getId(),
                        cat.getOwner().getName(),
                        cat.getOwner().getBirthday(),
                        userMapper.convertUserToUserDTO(cat.getOwner().getUser())),
                cat.getCats().stream().map(this::convertCatToCatDTO).toList());
    }

    public Cat convertCatDTOToCat(CatDTO catDTO) {
        if (catDTO == null) {
            return null;
        }
        return new Cat(catDTO.getId(),
                catDTO.getName(),
                catDTO.getBirthday(),
                catDTO.getColor(),
                catDTO.getBreed(),
                new Owner(catDTO.getOwner().getId(),
                        catDTO.getOwner().getName(),
                        catDTO.getOwner().getBirthday(),
                        userMapper.convertUserDTOToUser(catDTO.getOwner().getUserDTO())),
                catDTO.getCats().stream().map(this::convertCatDTOToCat).toList());
    }
}
