package com.example.demo.rest;

import com.example.demo.security.AuthRequest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor

@Component
public class AuthHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtUtil;

    public Mono<ServerResponse> signUp(ServerRequest request){
        Mono<User> userMono = request.bodyToMono(User.class);
        return userMono
                .map(user -> {
                    log.debug("User registering with username: "+user.getUsername());
                    return user;
                })
                .flatMap(userService::register)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage())));
    }

    public Mono<ServerResponse> signIn(ServerRequest request){
        Mono<AuthRequest> loginRequest = request.bodyToMono(AuthRequest.class);
        return loginRequest
                .map(authRequest -> {
                    log.debug("User logging-in with username: "+authRequest.email());
                    return authRequest;
                })
//                .flatMap(authRequest ->
//                        userDetailsService.findByUsername(authRequest.email())
//                            .filter(userDetails -> passwordEncoder.matches(authRequest.password(), userDetails.getPassword()))
//                            .filter(UserDetails::isEnabled)
//                            .filter(UserDetails::isAccountNonExpired)
//                            .filter(UserDetails::isAccountNonLocked)
//                            .filter(UserDetails::isCredentialsNonExpired)
//                )
                .flatMap(userService::login)
                .flatMap(userDetails -> ServerResponse.ok().header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtUtil.generateJwtToken(userDetails))).build())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username/Password is invalid")))
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username/Password is invalid")));
    }

    public Mono<ServerResponse> resetAccessToken(ServerRequest request){
        String refreshToken = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if(refreshToken == null) return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired or invalid. Please login."));

        refreshToken = refreshToken.substring(7);

        if(jwtUtil.isTokenValid(refreshToken)){
            String username = jwtUtil.getSubject(refreshToken);

            // find user by username & if exists - generate new token & return
            String finalRefreshToken = refreshToken;
            return userDetailsService.findByUsername(username)
                    .flatMap(userDetails -> ServerResponse.ok().header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtUtil.refreshAccessToken(userDetails, finalRefreshToken))).build())
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired or invalid. Please login.")));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired or invalid. Please login."));
    }

}
