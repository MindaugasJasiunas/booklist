package com.example.demo.bookuser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor

@Service
public class BookUserServiceImpl implements BookUserService{
    private final BookUserRepository bookUserRepository;

    @Override
    public Flux<BookUser> findAllBookUserByEmail(String email){
        return bookUserRepository.findAllByUserEmail(email);
    }

    @Override
    public Flux<BookUser> findAllBookUserByEmail(String email, PageRequest pagination){
        return bookUserRepository.findAllByUserEmailAndPaginate(email, pagination.getPageNumber()*pagination.getPageSize(), pagination.getPageSize());
    }

    @Override
    public Mono<BookUser> findBookUserByEmailAndISBN(String email, String isbn){
        return bookUserRepository.findByUserEmailAndBookISBN(email, isbn);
    }

    @Override
    public Mono<BookUser> saveToMyBooks(BookUser bookUser) {
        return Mono.just(bookUser)
                .filterWhen(this::bookNotExistsInMyBooks)
                .flatMap(bookUserRepository::save);
    }

    @Override
    public Mono<Void> deleteFromMyBooks(BookUser bookUser) {
        return Mono.just(bookUser)
                .filterWhen(bookUserToSave -> bookNotExistsInMyBooks(bookUserToSave).map(aBoolean -> !aBoolean))
                .map(bookUserObj -> bookUserObj.getId())
                .flatMap(bookUserRepository::deleteById);
    }

    @Override
    public Mono<Boolean> bookNotExistsInMyBooks(BookUser bookUser){
        return bookUserRepository.findByUserEmailAndBookISBN(bookUser.getUserEmail(), bookUser.getBookISBN())
                .flatMap(recordFromDB -> Mono.just(false))
                .switchIfEmpty(Mono.just(true));
    }
}
