package com.example.demo.bookuser;

import com.example.demo.book.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookUserService {
    Flux<BookUser> findAllBookUserByEmail(String email);
    Mono<BookUser> findAllBookUserByEmailAndISBN(String email, String isbn);
    Mono<BookUser> saveToMyBooks(BookUser bookUser);
    Mono<Void> deleteFromMyBooks(BookUser bookUser);
    Mono<Boolean> bookNotExistsInMyBooks(BookUser bookUser);
}
