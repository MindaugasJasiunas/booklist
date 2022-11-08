package com.example.demo.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@Slf4j

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    @Override
    public Mono<Book> getBook(String isbn) {
        log.debug("[BookServiceImpl] getBook(ISBN="+isbn+")");
        return bookRepository.findByISBN(isbn);
    }

    @Override
    public Mono<Page<Book>> getBooks(PageRequest pageRequest) {
        log.debug("[BookServiceImpl] getBooks(PageRequest="+pageRequest+")");
        return bookRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(bookRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }

    @Override
    public Mono<Page<Book>> getBooks(PageRequest pageRequest, String searchText) {
        log.debug("[BookServiceImpl] getBooks(PageRequest="+pageRequest+", searchText='"+searchText+"')");
        return bookRepository.findAllBySearchAndPaginate(searchText, pageRequest.getPageNumber()*pageRequest.getPageSize(), pageRequest.getPageSize())
                .collectList()
                .zipWith(bookRepository.findAllBySearch(searchText).count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }
}
