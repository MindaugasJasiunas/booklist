package com.example.demo.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProvider {
    private final String secretKey;

    public Set<String> getAuthoritiesFromToken(String token) {
        if(getClaims(token).get("authorities") == null) return Collections.emptySet();
        return Stream.of(getClaims(token).get("authorities"))
                .map(claims -> claims.asArray(String.class))
                .flatMap(Stream::of)
                .collect(Collectors.toSet());
    }

    public boolean isTokenValid(String token){
        String username;
        try{
            username = getJWTVerifier().verify(token).getSubject();
        }catch (TokenExpiredException | JWTDecodeException | SignatureVerificationException e){
            return false;
        }
        return ((username != null) && (username.trim() != "") && (!isTokenExpired(token)));
    }

    public String getSubject(String token){
        return getJWTVerifier().verify(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        Date expiration = getJWTVerifier().verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    public boolean authorizationHeaderHasAuthority(String jwt, String authority){
        if(jwt == null) return false;
        if(authority == null) return false;
        jwt=jwt.substring(7);
        if(!isTokenValid(jwt)) return false;
        Set<String> authorities = getAuthoritiesFromToken(jwt);
        return authorities.contains(authority);
    }

    private Map<String, Claim> getClaims(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaims();
    }

    private JWTVerifier getJWTVerifier(){
        JWTVerifier verifier;
        try{
            Algorithm algo = Algorithm.HMAC512(secretKey);
            verifier = JWT.require(algo).withIssuer("AppName").build();
        }catch (JWTVerificationException e){
            throw new JWTVerificationException("Token cannot be verified");
        }
        return verifier;
    }
}
