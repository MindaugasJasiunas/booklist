package com.example.demo.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class BookRoutesV1Config {

    @Bean
    public RouterFunction<ServerResponse> bookRoutesV1(BookHandler handler) {
        return RouterFunctions.route()
                .GET("api/v1/books", accept(APPLICATION_JSON), handler::getBooks)
                .GET("api/v1/books/{ISBN}", accept(APPLICATION_JSON), handler::getBookByISBN)
                .POST("api/v1/books", accept(APPLICATION_JSON), handler::createBook)
                .PUT("api/v1/books/{ISBN}", accept(APPLICATION_JSON), handler::updateBookByISBN)
                .PATCH("api/v1/books/{ISBN}", accept(APPLICATION_JSON), handler::patchBookByISBN)
                .DELETE("api/v1/books/{ISBN}", accept(APPLICATION_JSON), handler::deleteBookByISBN)
                .build();
    }
}