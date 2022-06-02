package ru.itmo.kotiki.service;

import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.exception.KotikiException;

import java.util.List;

public interface OwnerService {
    OwnerDTO getById(Long id) throws KotikiException;

    List<OwnerDTO> findAll();

    OwnerDTO save(OwnerDTO entity);

    OwnerDTO update(OwnerDTO entity);

    OwnerDTO delete(Long id);
}
