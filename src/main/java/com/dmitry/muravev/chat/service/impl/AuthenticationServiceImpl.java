package com.dmitry.muravev.chat.service.impl;

import com.dmitry.muravev.chat.dto.request.LoginRequest;
import com.dmitry.muravev.chat.dto.response.AuthenticationResponse;
import com.dmitry.muravev.chat.security.jwt.JwtTokenProvider;
import com.dmitry.muravev.chat.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticate user.
     *
     * @param request - {@link LoginRequest} contains required information for
     *                user authentication.
     * @return {@link AuthenticationResponse} contains user access token.
     */
    @Override
    public AuthenticationResponse authenticate(LoginRequest request) {
        String login = request.getLogin();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (request.getLogin(), request.getPassword()));
        } catch (AuthenticationException ae) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong credentials");
        }

        String accessToken = jwtTokenProvider.createToken(login);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .build();
    }

}
