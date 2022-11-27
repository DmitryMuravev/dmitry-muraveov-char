package com.dmitry.muravev.chat.security;

import com.dmitry.muravev.chat.entity.UserEntity;
import com.dmitry.muravev.chat.security.jwt.JwtUserFactory;
import com.dmitry.muravev.chat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JwtUserFactory jwtUserFactory;

    /**
     * Load {@link UserDetails}.
     *
     * @param login user login.
     * @return {@link UserDetails}.
     */
    @Override
    public UserDetails loadUserByUsername(String login)  {
        UserEntity user = userService.findUser(login);

        return jwtUserFactory.create(user);
    }
}
