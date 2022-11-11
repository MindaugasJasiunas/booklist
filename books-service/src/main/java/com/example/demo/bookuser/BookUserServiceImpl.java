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
    public Flux<BookUser> findAllBookUserByEmail(String email, boolean isWishlist){
        return bookUserRepository.findAllByUserEmail(email)
                .filter(bookUser -> bookUser.isWishlist() == isWishlist);
    }

    @Override
    public Flux<BookUser> findAllBookUserByEmail(String email, PageRequest pagination, boolean isWishlist){
        return bookUserRepository.findAllByUserEmailAndPaginate(email, isWishlist, pagination.getPageNumber()*pagination.getPageSize(), pagination.getPageSize());
    }

    @Override
    public Mono<BookUser> findBookUserByEmailAndISBN(String email, String isbn, boolean isWishlist){
        return bookUserRepository.findByUserEmailAndBookISBNAndWishlist(email, isbn, isWishlist);
    }

    @Override
    public Mono<BookUser> saveToMyBooksOrWishlist(BookUser bookUser, boolean isWishlist) {
        return Mono.just(bookUser)
                .map(bookUserToSave -> {
                    bookUserToSave.setWishlist(isWishlist);
                    return bookUserToSave;
                })
                .filterWhen(bookUserToSave -> bookNotExistsInMyBooksOrWishlist(bookUserToSave, isWishlist))
                .flatMap(bookUserRepository::save);
    }

    @Override
    public Mono<Void> deleteFromMyBooksOrWishlist(BookUser bookUser, boolean isWishlist) {
        return Mono.just(bookUser)
                .filterWhen(bookUserToDelete -> bookNotExistsInMyBooksOrWishlist(bookUserToDelete, isWishlist).map(aBoolean -> !aBoolean))
                .filter(bookUserToDelete -> bookUserToDelete.isWishlist() == isWishlist)
                .map(bookUserObj -> bookUserObj.getId())
                .flatMap(bookUserRepository::deleteById);
    }

    @Override
    public Mono<Boolean> bookNotExistsInMyBooksOrWishlist(BookUser bookUser, boolean isInWishlist){
        return bookUserRepository.findByUserEmailAndBookISBNAndWishlist(bookUser.getUserEmail(), bookUser.getBookISBN(), isInWishlist)
                .filter(bookUserFromDB -> bookUserFromDB.isWishlist() == isInWishlist)
                .flatMap(recordFromDB -> Mono.just(false))
                .switchIfEmpty(Mono.just(true));
    }
}
