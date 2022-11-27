package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.RegistrationRequest;
import com.dmitry.muravev.chat.entity.RoleEntity;
import com.dmitry.muravev.chat.entity.UserEntity;
import com.dmitry.muravev.chat.repository.RoleRepository;
import com.dmitry.muravev.chat.repository.UserRepository;
import com.dmitry.muravev.chat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    private static final String TEST_USER_LOGIN = "userLogin";
    private static final String TEST_USER_PASSWORD = "password";
    private static final UUID TEST_USER_ID = UUID.randomUUID();

    private Map<UUID, UserEntity> savedUsers;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @BeforeEach
    void init() {
        savedUsers = new HashMap<>();
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    @Test
    public void whenRegisterUserAndDefaultRoleExistsThenSaveUser() {
        RoleEntity defaultRole = new RoleEntity();

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenAnswer(i -> savedUsers.put(TEST_USER_ID, i.getArgument(0, UserEntity.class)));

        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(defaultRole));

        UserService userService = new UserServiceImpl(userRepository, roleRepository,
                new BCryptPasswordEncoder());

        userService.register(new RegistrationRequest(TEST_USER_LOGIN, TEST_USER_PASSWORD));

        assertTrue(savedUsers.values().size() > 0);
    }

    @Test
    public void whenRegisterUserWithAlreadyExistedLoginThenThrowException() {

        Mockito.when(userRepository.existsByLogin(Mockito.any())).thenReturn(true);

        UserService userService = new UserServiceImpl(userRepository, roleRepository,
                new BCryptPasswordEncoder());

        assertThrows(ResponseStatusException.class,
                () -> userService.register(new RegistrationRequest(TEST_USER_LOGIN, TEST_USER_PASSWORD)));
    }

    @Test
    public void whenRegisterUserAndDefaultRoleNotExistsThenThrowException() {

        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn(Optional.empty());

        UserService userService = new UserServiceImpl(userRepository, roleRepository,
                new BCryptPasswordEncoder());

        assertThrows(ResponseStatusException.class,
                () -> userService.register(new RegistrationRequest(TEST_USER_LOGIN, TEST_USER_PASSWORD)));
    }
}
