package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.RegistrationRequest;
import com.dmitry.muravev.chat.entity.UserEntity;

public interface UserService {

    /**
     * Register user.
     *
     * @param request {@link RegistrationRequest} contains required user registration info.
     */
    void register(RegistrationRequest request);

    /**
     * Find user entity.
     *
     * @param login user login.
     * @return {@link UserEntity}.
     */
    UserEntity findUser(String login);


}
