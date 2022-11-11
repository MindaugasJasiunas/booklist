package com.example.demo.bookuser;

import com.example.demo.book.Book;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookUserRepository extends ReactiveMongoRepository<BookUser, String> {
    Flux<BookUser> findAllByUserEmail(String email);

    @Aggregation(pipeline = {
            "{$match :  {'userEmail' : { $regex: '.*?0.*'} } }",
            "{ $skip : ?1 }",
            "{ $limit: ?2 }"
    })
    Flux<BookUser> findAllByUserEmailAndPaginate(String email, int skip, int limit);

    Mono<BookUser> findByUserEmailAndBookISBN(String userEmail, String ISBN);

}
