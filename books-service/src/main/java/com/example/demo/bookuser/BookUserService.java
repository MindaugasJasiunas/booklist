package com.example.demo.bookuser;

import com.example.demo.book.Book;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BookUserService {
    Flux<BookUser> findAllBookUserByEmail(String email, boolean isWishlist);
    Flux<BookUser> findAllBookUserByEmail(String email, PageRequest pagination, boolean isWishlist);
    Mono<BookUser> findBookUserByEmailAndISBN(String email, String isbn, boolean isWishlist);
    Mono<BookUser> saveToMyBooksOrWishlist(BookUser bookUser, boolean isWishlist);
    Mono<Void> deleteFromMyBooksOrWishlist(BookUser bookUser, boolean isWishlist);
    Mono<Boolean> bookNotExistsInMyBooksOrWishlist(BookUser bookUser, boolean isInWishlist);
}
