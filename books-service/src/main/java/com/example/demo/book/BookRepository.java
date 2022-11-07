package com.example.demo.book;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllBy(Pageable pageable);
    Mono<Book> findByISBN(String ISBN);
}
