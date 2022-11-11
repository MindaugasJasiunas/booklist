package com.example.demo.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MyBooksRoutesV1Config {
    @Bean
    public RouterFunction<ServerResponse> myBooksV1(MyBooksHandler handler) {
        return RouterFunctions.route()
                .GET("api/v1/my-books", accept(APPLICATION_JSON), handler::getMyBooks)
                .POST("api/v1/my-books/{ISBN}", accept(APPLICATION_JSON), handler::addToMyBooks)
                .DELETE("api/v1/my-books/{ISBN}", accept(APPLICATION_JSON), handler::removeFromMyBooks)

                .GET("api/v1/wishlist", accept(APPLICATION_JSON), handler::getWishlist)
                .POST("api/v1/wishlist/{ISBN}", accept(APPLICATION_JSON), handler::addToWishlist)
                .DELETE("api/v1/wishlist/{ISBN}", accept(APPLICATION_JSON), handler::removeFromWishlist)

                .build();
    }
}
