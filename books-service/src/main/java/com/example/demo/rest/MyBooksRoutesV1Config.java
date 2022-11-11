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
                .GET("api/v1/my-books", accept(APPLICATION_JSON), request -> handler.getMyBooksOrWishlist(request, false))
                .POST("api/v1/my-books/{ISBN}", accept(APPLICATION_JSON), request -> handler.addToMyBooksOrWishlist(request, false))
                .DELETE("api/v1/my-books/{ISBN}", accept(APPLICATION_JSON), request -> handler.removeFromMyBooksOrWishlist(request, false))

                .GET("api/v1/wishlist", accept(APPLICATION_JSON), request -> handler.getMyBooksOrWishlist(request, true))
                .POST("api/v1/wishlist/{ISBN}", accept(APPLICATION_JSON), request -> handler.addToMyBooksOrWishlist(request, true))
                .DELETE("api/v1/wishlist/{ISBN}", accept(APPLICATION_JSON), request -> handler.removeFromMyBooksOrWishlist(request, true))

                .build();
    }
}
