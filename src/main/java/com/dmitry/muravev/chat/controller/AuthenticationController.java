package com.dmitry.muravev.chat.controller;

import com.dmitry.muravev.chat.dto.request.LoginRequest;
import com.dmitry.muravev.chat.dto.response.AuthenticationResponse;
import com.dmitry.muravev.chat.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Handle user authentication request.
     *
     * @param request - {@link LoginRequest} contains required information for
     *                user authentication.
     * @return {@link AuthenticationResponse} contains user access token.
     */
    @PostMapping("/login")
    public AuthenticationResponse authenticate(@RequestBody @Valid LoginRequest request) {
        return authenticationService.authenticate(request);
    }
}
