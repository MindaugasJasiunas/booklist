package com.example.demo.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    @Override
    public Mono<Book> getBook(String isbn) {
        return bookRepository.findByISBN(isbn);
    }

    @Override
    public Mono<Page<Book>> getBooks(PageRequest pageRequest) {
        return bookRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(bookRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }
}
