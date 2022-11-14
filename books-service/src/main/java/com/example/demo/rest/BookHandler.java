package com.example.demo.rest;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.book.Book;
import com.example.demo.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor

@Component
public class BookHandler {
    private final BookService service;
    private final JwtTokenProvider jwtTokenProvider;

    public Mono<ServerResponse> getBookByISBN(ServerRequest request){
        log.debug("[BookHandler] getBookByISBN("+request.pathVariable("ISBN")+")");

        String isbn = request.pathVariable("ISBN");
        return service.getBook(isbn)
                .flatMap(book -> ServerResponse.ok().bodyValue(book))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getBooks(ServerRequest request){
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

        if(request.queryParam("search").isPresent()){
            String searchText = request.queryParam("search").get();
            log.debug("[BookHandler] getBooks(page="+page+", size="+size+", sortField="+sortFieldName+", search="+searchText+")");
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(service.getBooks(PageRequest.of(page, size).withSort(Sort.by(sortFieldName).descending()), searchText), new ParameterizedTypeReference<Page<Book>>(){}));
        }
        log.debug("[BookHandler] getBooks(page="+page+", size="+size+", sortField="+sortFieldName+")");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(service.getBooks(PageRequest.of(page, size).withSort(Sort.by(sortFieldName).descending())), new ParameterizedTypeReference<Page<Book>>(){}));
    }



    public Mono<ServerResponse> createBook(ServerRequest request){
        log.debug("[BookHandler] createBook()");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:create")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        return request.bodyToMono(Book.class)
                .map(book -> {
                    log.debug("[BookHandler] createBook("+book+") called");
                    return book;
                })
                .flatMap(service::createBook)
                .map(book -> {
                    log.debug("[BookHandler] createBook(): createdBook URI: http://localhost:9090/api/v1/books/" + book.getISBN());
                    return book;
                })
                .flatMap(createdBook -> ServerResponse.created(URI.create("http://localhost:9090/api/v1/books/" + createdBook.getISBN())).build())
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage())));
    }

    public Mono<ServerResponse> updateBookByISBN(ServerRequest request){
        log.debug("[BookHandler] updateBookByISBN("+request.pathVariable("ISBN")+")");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:update")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String isbn = request.pathVariable("ISBN");
        log.debug("[BookHandler] updateBookByISBN("+isbn+") called");
        return request.bodyToMono(Book.class)
                .flatMap(bookToSave -> service.updateBook(bookToSave, isbn))
                .flatMap(savedBook -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage())));
    }

    public Mono<ServerResponse> patchBookByISBN(ServerRequest request){
        log.debug("[BookHandler] patchBookByISBN("+request.pathVariable("ISBN")+")");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:update")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String isbn = request.pathVariable("ISBN");
        log.debug("[BookHandler] patchBookByISBN("+isbn+") called");
        return request.bodyToMono(Book.class)
                .flatMap(bookToSave -> service.patchBook(bookToSave, isbn))
                .flatMap(savedUser -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage())));
    }

    public Mono<ServerResponse> deleteBookByISBN(ServerRequest request){
        log.debug("[BookHandler] deleteBookByISBN("+request.pathVariable("ISBN")+")");
        if(!jwtTokenProvider.authorizationHeaderHasAuthority(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), "book:delete")) return ServerResponse.status(HttpStatus.FORBIDDEN).build();

        String isbn = request.pathVariable("ISBN");
        log.debug("[BookHandler] deleteBookByISBN("+isbn+") called");
        return service.deleteBook(isbn)
                .flatMap(unused -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.noContent().build());
    }

}
