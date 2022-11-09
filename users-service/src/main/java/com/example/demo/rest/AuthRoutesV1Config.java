package com.example.demo.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AuthRoutesV1Config {

    @Bean
    public RouterFunction<ServerResponse> userRoutesV1(AuthHandler handler) {
        return RouterFunctions.route()
                .POST("login", accept(APPLICATION_JSON), handler::signIn)
                .POST("register", accept(APPLICATION_JSON), handler::signUp)
                .POST("resettoken", accept(APPLICATION_JSON), handler::resetAccessToken)
                .build();
    }
}
