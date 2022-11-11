package com.example.demo.bookuser;

import com.example.demo.book.Book;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookUserRepository extends ReactiveMongoRepository<BookUser, String> {
    Flux<BookUser> findAllByUserEmail(String email);

    @Aggregation(pipeline = {
            "{$match :  {'userEmail' : '?0'} }",
            "{$match :  {'isWishlist' : ?1 } }",
            "{ $skip : ?2 }",
            "{ $limit: ?3 }"
    })
    Flux<BookUser> findAllByUserEmailAndPaginate(String email, boolean isWishlist, int skip, int limit);

    @Query("{ 'userEmail' :  '?0', 'bookISBN' : '?1', 'isWishlist' : ?2 }")
    Mono<BookUser> findByUserEmailAndBookISBNAndWishlist(String userEmail, String ISBN, boolean isWishlist);

}
