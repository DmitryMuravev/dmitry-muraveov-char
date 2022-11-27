package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.LoginRequest;
import com.dmitry.muravev.chat.security.jwt.JwtTokenProvider;
import com.dmitry.muravev.chat.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


public class AuthenticationServiceTest {

    @Test
    public void whenAuthenticateAndUserCredentialsIncorrectThenThrowException() {
        LoginRequest loginRequest = new LoginRequest("login", "password");

        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);

        AuthenticationService authenticationService =
                new AuthenticationServiceImpl(jwtTokenProvider, authenticationManager);

        assertThrows(ResponseStatusException.class, () -> authenticationService.authenticate(loginRequest));
    }
}
