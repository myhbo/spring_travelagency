package com.training.spring_travelagency.service;

import com.training.spring_travelagency.dto.NewUserDTO;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUser() {
        NewUserDTO newUserDTO = createTestNewUser();
        userService.createUser(newUserDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    private static NewUserDTO createTestNewUser() {
        return NewUserDTO.builder()
                .email("user@user.test")
                .password("admin")
                .fullName("Petrov Petr")
                .build();
    }

    private static User createTestUser() {
        return User.builder()
                .id(1L)
                .password("admin")
                .email("user@user.test")
                .fullName("Petrov Petr")
                .enabled(true)
                .build();
    }

    @Test
    public void banUser() {
        User user = createTestUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.banUser(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void unbanUser() {
        User user = createTestUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.unbanUser(user.getId());
        verify(userRepository, times(1)).save(user);
    }
}