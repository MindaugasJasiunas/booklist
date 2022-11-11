package com.example.demo.rest;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.book.BookService;
import com.example.demo.bookuser.BookUser;
import com.example.demo.bookuser.BookUserService;
import com.example.demo.error.ForbiddenError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    public Mono<ServerResponse> getMyBooksOrWishlist(ServerRequest request, boolean isWishlist){
        log.debug(isWishlist ? "[MyBooksHandler] getMyBooksOrWISHLIST" : "[MyBooksHandler] getMyBOOKSOrWishlist");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) throw new ForbiddenError("Expired access token or insufficient authorities");

        int page = 0;
        int size = 10;
        String sortFieldName = "title";

        if(request.queryParam("page").isPresent()){
            try{
                page = Integer.parseInt(request.queryParam("page").get());
            }catch (NumberFormatException e){}
        }
        if(request.queryParam("size").isPresent()){
            try{
                size = Integer.parseInt(request.queryParam("size").get());
            }catch (NumberFormatException e){}
        }
        if(request.queryParam("sort").isPresent()){
            sortFieldName = request.queryParam("sort").get();
        }

        PageRequest pagination = PageRequest.of(page, size, Sort.by(sortFieldName));

        String subject = jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7));

        return bookUserService
                .findAllBookUserByEmail(subject, pagination, isWishlist)
                .flatMap(bookUser -> bookService.getBook(bookUser.getBookISBN()))
                .collectList()
                .zipWith(bookUserService.findAllBookUserByEmail(subject, isWishlist).count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pagination, tuple.getT2()))
                .flatMap(booksList ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(booksList)));
    }

    public Mono<ServerResponse> addToMyBooksOrWishlist(ServerRequest request, boolean isWishlist){
        log.debug(isWishlist ? "[MyBooksHandler] addToMyBooksOrWISHLIST" : "[MyBooksHandler] addToMyBOOKSOrWishlist");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) throw new ForbiddenError("Expired access token or insufficient authorities");

        String userEmail = jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7));
        String bookISBN = request.pathVariable("ISBN");
        BookUser record = new BookUser(null, userEmail, bookISBN, false);

        return Mono.just(record)
                .filterWhen(bookUser -> bookUserService.bookNotExistsInMyBooksOrWishlist(bookUser, isWishlist))
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Already in my books")))
                .filterWhen(bookUserToSave -> bookService.bookNotExistsInDBByISBN(bookUserToSave.getBookISBN()).map(aBoolean -> !aBoolean)) // if book exists
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Book doesn't exist")))
                .flatMap(bookUser -> bookUserService.saveToMyBooksOrWishlist(bookUser, isWishlist))
                .flatMap(savedBookUser -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> removeFromMyBooksOrWishlist(ServerRequest request, boolean isWishlist){
        log.debug(isWishlist ? "[MyBooksHandler] removeFromMyBooksOrWISHLIST" : "[MyBooksHandler] removeFromMyBOOKSOrWishlist");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:read")) throw new ForbiddenError("Expired access token or insufficient authorities");

        String userEmail = jwtTokenProvider.getSubject(request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7));
        String bookISBN = request.pathVariable("ISBN");

        // if exists - remove from DB
        return bookUserService.findBookUserByEmailAndISBN(userEmail, bookISBN, isWishlist)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Doesn't exist in my books")))
                .flatMap(bookUserToDelete -> bookUserService.deleteFromMyBooksOrWishlist(bookUserToDelete, isWishlist))
                .flatMap(unused -> ServerResponse.noContent().build());
    }

}
