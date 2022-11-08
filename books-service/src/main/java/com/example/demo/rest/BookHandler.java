package com.example.demo.rest;

import com.example.demo.book.Book;
import com.example.demo.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor

@Component
public class BookHandler {
    private final BookService service;

    public Mono<ServerResponse> getBookByISBN(ServerRequest request){
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
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(service.getBooks(PageRequest.of(page, size).withSort(Sort.by(sortFieldName).descending()), request.queryParam("search").get()), new ParameterizedTypeReference<Page<Book>>(){}));
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(service.getBooks(PageRequest.of(page, size).withSort(Sort.by(sortFieldName).descending())), new ParameterizedTypeReference<Page<Book>>(){}));
    }
}
