package com.dmitry.muravev.chat.config;

import com.dmitry.muravev.chat.security.jwt.JwtConfigurer;
import com.dmitry.muravev.chat.security.jwt.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties("jwt.token")
public class JwtConfig {

    @Getter
    @Setter
    private String secret;

    @Getter
    @Setter
    private Long expirationTime;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(UserDetailsService userDetailsService) {
        return new JwtTokenProvider(userDetailsService, this);
    }

    @Bean
    public JwtConfigurer jwtConfigurer(JwtTokenProvider jwtTokenProvider) {
        return new JwtConfigurer(jwtTokenProvider);
    }
}