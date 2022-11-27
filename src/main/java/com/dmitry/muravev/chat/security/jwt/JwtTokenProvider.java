package com.dmitry.muravev.chat.security.jwt;

import com.dmitry.muravev.chat.config.JwtConfig;
import com.dmitry.muravev.chat.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String USER_LOGIN_KEY = "name";
    private final UserDetailsService userDetailsService;
    private final JwtConfig jwtConfig;

    /**
     * Create user access token by username.
     *
     * @param login username.
     * @return access token.
     */
    public String createToken(String login) {
        Claims claims = Jwts.claims();
        claims.put(USER_LOGIN_KEY, login);

        Date creationTime = new Date();
        Date expireTime = Date.from(LocalDateTime.now()
                .plusSeconds(jwtConfig.getExpirationTime()).toInstant(ZoneOffset.UTC));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(creationTime)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    /**
     * Map access token to {@link Authentication}.
     *
     * @param token access token.
     * @return {@link Authentication}.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(getLogin(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    /**
     * Get token from request.
     *
     * @param request {@link HttpServletRequest}.
     * @return {@link Optional} of string possibly contains access token.
     */
    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    /**
     * validate access token.
     *
     * @param token string to be validated as access token.
     * @return true if token is valid.
     * @throws ResponseStatusException if token can not be validated.
     */
    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Unable to validate token", ex);
        }
        return false;
    }

    /**
     * Get user login from token.
     *
     * @param token access token to be processed.
     * @return user login from token.
     */
    public String getLogin(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token).getBody().get(USER_LOGIN_KEY, String.class);
    }

    /**
     * Map {@link RoleEntity} set to string list of string role names.
     *
     * @param roles set of {@link RoleEntity}.
     * @return list of string role names.
     */
    public List<String> getRoleNames(Set<RoleEntity> roles) {
        return roles.stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
