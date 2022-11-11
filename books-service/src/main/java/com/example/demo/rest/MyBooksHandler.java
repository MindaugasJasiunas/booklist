package com.example.demo.rest;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.book.BookService;
import com.example.demo.bookuser.BookUser;
import com.example.demo.bookuser.BookUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor

@Component
public class MyBooksHandler {
    private final BookService bookService;
    private final BookUserService bookUserService;
    private final JwtTokenProvider jwtTokenProvider;

    public Mono<ServerResponse> getMyBooks(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        // TODO: paginate returning books
        // TODO: implement Wishlist

        return bookUserService
                .findAllBookUserByEmail(jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7)))
                .flatMap(bookUser -> bookService.getBook(bookUser.getBookISBN()))
                .collectList()
                .flatMap(booksList -> ServerResponse.ok().body(BodyInserters.fromValue(booksList)));
    }

    public Mono<ServerResponse> addToMyBooks(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String userEmail = jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7));
        String bookISBN = request.pathVariable("ISBN");
        BookUser record = new BookUser(null, userEmail, bookISBN);

        return Mono.just(record)
                .filterWhen(bookUserService::bookNotExistsInMyBooks)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Already in my books")))
                .filterWhen(bookUserToSave -> bookService.bookNotExistsInDBByISBN(bookUserToSave.getBookISBN()))
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Book doesn't exist")))
                .flatMap(bookUserService::saveToMyBooks)
                .flatMap(savedBookUser -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> removeFromMyBooks(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String userEmail = jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7));
        String bookISBN = request.pathVariable("ISBN");

        // if exists - remove from DB
        return bookUserService.findAllBookUserByEmailAndISBN(userEmail, bookISBN)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Doesn't exist in my books")))
                .flatMap(bookUserService::deleteFromMyBooks)
                .flatMap(unused -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> getWishlist(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        return ServerResponse.notFound().build();
    }

    public Mono<ServerResponse> addToWishlist(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String isbn = request.pathVariable("ISBN");
        return ServerResponse.notFound().build();
    }

    public Mono<ServerResponse> removeFromWishlist(ServerRequest request){
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String isbn = request.pathVariable("ISBN");
        return ServerResponse.notFound().build();
    }

}
