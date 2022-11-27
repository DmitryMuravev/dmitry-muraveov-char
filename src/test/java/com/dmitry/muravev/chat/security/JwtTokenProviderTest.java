package com.dmitry.muravev.chat.security;

import com.dmitry.muravev.chat.config.JwtConfig;
import com.dmitry.muravev.chat.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JwtTokenProviderTest {

    private static final String SECRET = "secret";
    private static final Long TOKEN_EXPIRATION_TIME = 3000L;

    private JwtConfig jwtConfig;
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void init() {
        userDetailsService = Mockito.mock(JwtUserDetailsService.class);
        jwtConfig = Mockito.mock(JwtConfig.class);
    }

    @Test
    public void whenCreateTokenThenTokenIsValid() {
        Mockito.when(jwtConfig.getExpirationTime()).thenReturn(TOKEN_EXPIRATION_TIME);
        Mockito.when(jwtConfig.getSecret()).thenReturn(SECRET);

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService, jwtConfig);

        assertTrue(jwtTokenProvider.validateAccessToken(jwtTokenProvider.createToken("login")));
    }

    @Test
    public void whenResolveTokenAndTokenHeaderCorrectThenReturnTokenValue() {
        String tokenValue = "TokenValue";
        String tokenHeader = "Bearer " + tokenValue;

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService, jwtConfig);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(tokenHeader);

        assertEquals(tokenValue, jwtTokenProvider.resolveToken(request).orElse(""));
    }
}
