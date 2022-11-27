package com.dmitry.muravev.chat.security.jwt;

import com.dmitry.muravev.chat.entity.RoleEntity;
import com.dmitry.muravev.chat.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUserFactory {

    /**
     * Map {@link UserEntity} to {@link JwtUser} .
     *
     * @param user {@link UserEntity} to be mapped to {@link JwtUser}.
     * @return {@link JwtUser}.
     */
    public JwtUser create(UserEntity user) {
        return JwtUser.builder()
                .login(user.getLogin())
                .authorities(mapToGrantedAuthorities(user.getRoles()))
                .password(user.getPassword())
                .build();
    }

    /**
     * Map user {@link RoleEntity roles} to {@link GrantedAuthority}.
     *
     * @param userRoles set of {@link RoleEntity}.
     * @return list of {@link GrantedAuthority}.
     */
    private List<GrantedAuthority> mapToGrantedAuthorities(Set<RoleEntity> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


}
