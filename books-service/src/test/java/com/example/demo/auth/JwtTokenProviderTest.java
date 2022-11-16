package com.example.demo.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    private String secretKey = "SuperSecretFakeKey123456789$#@";
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey);
    private int expirationAfter10MinsInMillis = 600000;
    private List<String> authorities = Arrays.asList("book:read", "book:create", "book:update", "book:delete");
    private String subject = "TestUser";

    @Test
    void getAuthoritiesFromToken() {
        Set<String> authorities = jwtTokenProvider.getAuthoritiesFromToken(getToken(false));
        System.out.println(authorities);
        authorities.forEach(authority -> {
            assertTrue(authorities.contains(authority), "extracted authorities missing authority "+authority);
        });
    }

    @Test
    void isTokenValid() {
        assertTrue(jwtTokenProvider.isTokenValid(getToken(false)));
        assertFalse(jwtTokenProvider.isTokenValid(getToken(true)), "token should be expired");
        assertFalse(jwtTokenProvider.isTokenValid(getToken(false)+"abc"), "token should be malformed");
    }

    @Test
    void getSubject() {
        assertEquals(subject, jwtTokenProvider.getSubject(getToken(false)));
    }

    @Test
    void isTokenExpired() {
        assertThrows(TokenExpiredException.class, () -> jwtTokenProvider.isTokenExpired(getToken(true)));
        assertFalse(jwtTokenProvider.isTokenExpired(getToken(false)));
    }

    @Test
    void authorizationHeaderHasAuthority() {
        assertTrue(jwtTokenProvider.authorizationHeaderHasAuthority("Bearer "+getToken(false), "book:read"));
        assertTrue(jwtTokenProvider.authorizationHeaderHasAuthority("Bearer "+getToken(false), "book:create"));
        assertTrue(jwtTokenProvider.authorizationHeaderHasAuthority("Bearer "+getToken(false), "book:update"));
        assertTrue(jwtTokenProvider.authorizationHeaderHasAuthority("Bearer "+getToken(false), "book:delete"));
        assertFalse(jwtTokenProvider.authorizationHeaderHasAuthority("Bearer "+getToken(false), "random"));
    }


    private String getToken(boolean isExpired){
        return JWT.create()
                .withIssuer("AppName")
                .withAudience("AppName Administration")
                .withIssuedAt(new Date())
                .withSubject(subject)
                .withArrayClaim("authorities", authorities.toArray(new String[0]))
                .withExpiresAt(isExpired? new Date(System.currentTimeMillis()-10000) : new Date(System.currentTimeMillis() + expirationAfter10MinsInMillis))
                .sign(Algorithm.HMAC512(secretKey));
    }
}