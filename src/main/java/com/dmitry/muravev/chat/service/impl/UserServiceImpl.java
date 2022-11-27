package com.dmitry.muravev.chat.service.impl;

import com.dmitry.muravev.chat.dto.request.RegistrationRequest;
import com.dmitry.muravev.chat.entity.RoleEntity;
import com.dmitry.muravev.chat.entity.UserEntity;
import com.dmitry.muravev.chat.repository.RoleRepository;
import com.dmitry.muravev.chat.repository.UserRepository;
import com.dmitry.muravev.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${registration.default-role}")
    private String USER_DEFAULT_ROLE;

    /**
     * Handle user registration requests.
     *
     * @param request {@link RegistrationRequest} contains required user registration info.
     * @throws ResponseStatusException if user default role not founded or user with same login already exists.
     */
    @Override
    public void register(RegistrationRequest request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "User with login " + request.getLogin() + " already exists");
        }

        Set<RoleEntity> userRoles = Collections
                .singleton(roleRepository.findByName(USER_DEFAULT_ROLE)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed, try again later")));

        UserEntity user = UserEntity.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(userRoles)
                .build();

        userRepository.save(user);
    }

    /**
     * Find user entity.
     *
     * @param login user login.
     * @return {@link UserEntity}.
     * @throws ResponseStatusException if user with provided login not found.
     */
    @Override
    public UserEntity findUser(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "User with login " + login + " not founded"));
    }
}
