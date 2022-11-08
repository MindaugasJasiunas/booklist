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

    @Override
    public Mono<Book> createBook(Book book) {
        log.debug("[BookServiceImpl] createBook(book="+book+")");
        Mono<Book> bookMono = Mono.just(book);
        return bookMono
                .filterWhen(this::bookNotExistsInDBByISBN)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Book with provided ISBN already exists")))
                .flatMap(bookRepository::save);
    }

    @Override
    public Mono<Book> updateBook(Book book, String isbn) {
        log.debug("[BookServiceImpl] updateBook(book="+book+", ISBN="+isbn+")");
        return bookRepository.findByISBN(isbn)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Book doesn't exist")))
                .flatMap(existingBook -> {
                    book.setId(existingBook.getId());
                    book.setISBN(existingBook.getISBN());
                    return bookRepository.save(book);
                });
    }

    @Override
    public Mono<Book> patchBook(Book book, String isbn) {
        log.debug("[BookServiceImpl] patchBook(book="+book+", ISBN="+isbn+")");
        return bookRepository.findByISBN(isbn)
                .flatMap(existingBook -> {
                    if(book.getAuthor() != null){
                        existingBook.setAuthor(book.getAuthor());
                    }
                    if(book.getTitle() != null){
                        existingBook.setTitle(book.getTitle());
                    }
                    if(book.getPages() != 0){
                        existingBook.setPages(book.getPages());
                    }
                    if(book.getImageUrl() != null){
                        existingBook.setImageUrl(book.getImageUrl());
                    }
                    if(book.getISBN() != null){
                        existingBook.setISBN(book.getISBN());
                    }
                    return bookRepository.save(existingBook);
                })
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Book doesn't exist")) );
    }

    @Override
    public Mono<Void> deleteBook(String isbn) {
        log.debug("[BookServiceImpl] deleteBook(ISBN="+isbn+")");
        return bookRepository.deleteByISBN(isbn);
    }


    private Mono<Boolean> bookNotExistsInDBByISBN(Book book){
        return bookRepository.findByISBN(book.getISBN())
                .flatMap(userFromDB -> Mono.just(false))
                .switchIfEmpty(Mono.just(true));
    }
}
