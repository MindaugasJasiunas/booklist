package com.example.demo.bookuser;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookUserRepository extends ReactiveMongoRepository<BookUser, String> {
    Flux<BookUser> findAllByUserEmail(String email);
    Mono<BookUser> findByUserEmailAndBookISBN(String userEmail, String ISBN);
}
