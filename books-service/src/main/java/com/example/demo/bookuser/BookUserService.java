package com.example.demo.bookuser;

import com.example.demo.book.Book;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookUserService {
    Flux<BookUser> findAllBookUserByEmail(String email);
    Flux<BookUser> findAllBookUserByEmail(String email, PageRequest pagination);
    Mono<BookUser> findBookUserByEmailAndISBN(String email, String isbn);
    Mono<BookUser> saveToMyBooks(BookUser bookUser);
    Mono<Void> deleteFromMyBooks(BookUser bookUser);
    Mono<Boolean> bookNotExistsInMyBooks(BookUser bookUser);
}
