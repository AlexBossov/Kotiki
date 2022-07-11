package ru.itmo.kotiki.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.dto.UserDTO;
import ru.itmo.kotiki.entity.Owner;
import ru.itmo.kotiki.mapper.OwnerMapper;
import ru.itmo.kotiki.mapper.UserMapper;
import ru.itmo.kotiki.exception.KotikiException;
import ru.itmo.kotiki.repository.OwnerRepository;
import ru.itmo.kotiki.service.OwnerService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository,
                            OwnerMapper ownerMapper,
                            PasswordEncoder passwordEncoder,
                            UserMapper userMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @KafkaListener(topics = "ownerGetById", containerFactory = "requestListenerContainerFactory")
    @SendTo
    @Transactional
    public OwnerDTO getById(Long id) throws KotikiException {
        try {
            return ownerMapper.convertOwnerToOwnerDTO(ownerRepository.getById(id));
        } catch (EntityNotFoundException e) {
            throw new KotikiException("Owner with the id doesn't exist");
        }
    }

    @KafkaListener(topics = "getOwners", containerFactory = "requestListenerContainerFactory")
    @SendTo
    @Transactional
    public List<OwnerDTO> findAll() {
        return ownerRepository.findAll().stream().map(ownerMapper::convertOwnerToOwnerDTO).toList();
    }

    @KafkaListener(topics = "saveOwner", containerFactory = "requestListenerContainerFactory")
    @Transactional
    public OwnerDTO save(OwnerDTO entity) {
        return ownerMapper.convertOwnerToOwnerDTO(ownerRepository.save(ownerMapper.convertOwnerDTOToOwner(entity)));
    }

    @KafkaListener(topics = "updateOwner", containerFactory = "requestListenerContainerFactory")
    @Transactional
    public OwnerDTO update(OwnerDTO entity) {
        try {
            Owner oldOwner = ownerRepository.getById(entity.getId());

            String name = oldOwner.getName();
            LocalDate birthday = oldOwner.getBirthday();
            UserDTO user = userMapper.convertUserToUserDTO(oldOwner.getUser());

            if (entity.getName() != null) {
                name = entity.getName();
            }

            if (entity.getBirthday() != null) {
                birthday = entity.getBirthday();
            }

            if (entity.getUserDTO() != null) {
                user = new UserDTO(entity.getId(),
                        entity.getUserDTO().getUsername(),
                        entity.getUserDTO().getPassword(),
                        entity.getUserDTO().getRoles());
            }

            var updatedOwnerDTO = new OwnerDTO(entity.getId(), name, birthday, user);
            return ownerMapper.convertOwnerToOwnerDTO(ownerRepository.save(ownerMapper.convertOwnerDTOToOwner(updatedOwnerDTO)));
        } catch (Exception e) {
            throw new KotikiException("Owner with the id doesn't exist");
        }
    }

    @Transactional
    @KafkaListener(topics = "deleteOwnerById", containerFactory = "requestListenerContainerFactory")
    public OwnerDTO delete(Long id) {
        OwnerDTO owner = ownerMapper.convertOwnerToOwnerDTO(ownerRepository.getById(id));
        ownerRepository.deleteById(id);
        return owner;
    }
}

