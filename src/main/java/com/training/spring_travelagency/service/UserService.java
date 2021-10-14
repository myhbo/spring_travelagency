package com.training.spring_travelagency.service;

import com.training.spring_travelagency.dto.NewUserDTO;
import com.training.spring_travelagency.dto.UserDTO;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       MessageSource messageSource,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmail(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(NewUserDTO userDTO) {
        User user = User
                .builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .role(Roles.USER)
                .enabled(true)
                .build();
        try {
            userRepository.save(user);
            log.info("New user " + user);
        } catch (Exception e) {
            log.error("Email not unique: " + userDTO.getEmail());
        }
    }

    public void updateUser(long id, UserDTO userDTO) {
        User user = getUserById(id);
        user.setEmail(userDTO.getEmail());
        if (Objects.nonNull(userDTO.getPassword()) && userDTO.getPassword().length() > 0) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFullName(userDTO.getFullName());
        user.setRole(userDTO.getRole());

        try {
            userRepository.save(user);
            log.info("Updated user " + user);
        } catch (Exception e) {
            log.error("Email not unique: " + userDTO.getEmail());
        }
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));
    }

    public void banUser(long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("Banned user " + user);
    }

    public void unbanUser(long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
        log.info("Unbanned user " + user);
    }
}
