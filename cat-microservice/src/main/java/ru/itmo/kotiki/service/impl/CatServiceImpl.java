package ru.itmo.kotiki.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.itmo.kotiki.exception.KotikiException;
import ru.itmo.kotiki.repository.CatRepository;
import ru.itmo.kotiki.service.CatService;
import ru.itmo.kotiki.dto.CatDTO;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.entity.Cat;
import ru.itmo.kotiki.enums.Color;
import ru.itmo.kotiki.mapper.CatMapper;
import ru.itmo.kotiki.mapper.OwnerMapper;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;
    private final CatMapper catMapper;
    private final OwnerMapper ownerMapper;

    @Autowired
    public CatServiceImpl(CatRepository catRepository,
                          CatMapper catMapper,
                          OwnerMapper ownerMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
        this.ownerMapper = ownerMapper;
    }

    @KafkaListener(topics = "getCatById", containerFactory = "kafkaListenerContainerFactory")
    public CatDTO getById(Long id) {
        try {
            return catMapper.convertCatToCatDTO(catRepository.getById(id));
        } catch (EntityNotFoundException exception) {
            throw new KotikiException("Cat with the id doesn't exist");
        }
    }

    @KafkaListener(topics = "findCats", containerFactory = "kafkaListenerContainerFactory")
    public List<CatDTO> findAll(String username) {
        return catRepository
                .findCatByOwner_User_Username(username)
                .stream()
                .map(catMapper::convertCatToCatDTO)
                .toList();
    }

    @KafkaListener(topics = "findCatsByBreed", containerFactory = "kafkaListenerContainerFactory")
    public List<CatDTO> findByBreed(String breed, String username) {
        return catRepository
                .findCatByBreedAndOwner_User_Username(breed, username)
                .stream().map(catMapper::convertCatToCatDTO)
                .toList();
    }

    @KafkaListener(topics = "findCatsByColor", containerFactory = "kafkaListenerContainerFactory")
    public List<CatDTO> findByColor(Color color, String username) {
        return catRepository
                .findCatByColorAndOwner_User_Username(color, username)
                .stream().map(catMapper::convertCatToCatDTO)
                .toList();
    }

    @KafkaListener(topics = "saveCat", containerFactory = "kafkaListenerContainerFactory")
    public CatDTO save(CatDTO entity) {
        return catMapper.convertCatToCatDTO(catRepository.save(catMapper.convertCatDTOToCat(entity)));
    }

    @Transactional
    @KafkaListener(topics = "updateCat", containerFactory = "kafkaListenerContainerFactory")
    public CatDTO update(CatDTO entity) {
        try {
            Cat oldCat = catRepository.getById(entity.getId());

            String name = oldCat.getName();
            LocalDate birthday = oldCat.getBirthday();
            String breed = oldCat.getBreed();
            Color color = oldCat.getColor();
            OwnerDTO owner = ownerMapper.convertOwnerToOwnerDTO(oldCat.getOwner());
            List<CatDTO> cats = oldCat.getCats().stream().map(catMapper::convertCatToCatDTO).toList();

            if (entity.getName() != null) {
                name = entity.getName();
            }

            if (entity.getBirthday() != null) {
                birthday = entity.getBirthday();
            }

            if (entity.getBreed() != null) {
                breed = entity.getBreed();
            }

            if (entity.getColor() != null) {
                color = entity.getColor();
            }

            if (entity.getOwner() != null) {
                owner = entity.getOwner();
            }

            if (entity.getCats() != null) {
                cats = entity.getCats();
            }

            var updatedCatDTO = new CatDTO(entity.getId(), name, birthday, color, breed, owner, cats);
            return catMapper.convertCatToCatDTO(catRepository.save(catMapper.convertCatDTOToCat(updatedCatDTO)));
        } catch (Exception e) {
            throw new KotikiException("Cat with the id doesn't exist");
        }
    }

    @Transactional
    @KafkaListener(topics = "deleteCatById", containerFactory = "kafkaListenerContainerFactory")
    public CatDTO delete(Long id) {
        CatDTO cat = catMapper.convertCatToCatDTO(catRepository.getById(id));
        catRepository.deleteById(id);
        return cat;
    }
}