package com.example.demo.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface BookService {
    Mono<Book> getBook(String isbn);
    Mono<Page<Book>> getBooks(PageRequest pageRequest);
    Mono<Page<Book>> getBooks(PageRequest pageRequest, String searchText);
    Mono<Book> createBook(Book book);
    Mono<Book> updateBook(Book book, String isbn);
    Mono<Book> patchBook(Book book, String isbn);
    Mono<Void> deleteBook(String isbn);
}
