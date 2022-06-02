package ru.itmo.kotiki.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.kotiki.dto.RoleDTO;
import ru.itmo.kotiki.dto.UserDTO;
import ru.itmo.kotiki.entity.Role;
import ru.itmo.kotiki.entity.User;
import ru.itmo.kotiki.mapper.UserMapper;
import ru.itmo.kotiki.repository.RoleRepository;
import ru.itmo.kotiki.repository.UserRepository;
import ru.itmo.kotiki.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        User newUser = userMapper.convertUserDTOToUser(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return userMapper.convertUserToUserDTO(newUser);
    }

    @Override
    public RoleDTO saveRole(RoleDTO role) {
        Role newRole = new Role(role.getId(), role.getName());
        roleRepository.save(newRole);
        return role;
    }

    @Override
    public void addRoleTOUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public UserDTO getUser(String username) {
        return userMapper.convertUserToUserDTO(userRepository.findByUsername(username));
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(userMapper::convertUserToUserDTO).toList();
    }
}
