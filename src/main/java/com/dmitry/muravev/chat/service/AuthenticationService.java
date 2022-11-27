package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.LoginRequest;
import com.dmitry.muravev.chat.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    /**
     * Authenticate user.
     *
     * @param request - {@link LoginRequest} contains required information for
     *                user authentication.
     * @return {@link AuthenticationResponse} contains user access token.
     */
    AuthenticationResponse authenticate(LoginRequest request);
}
