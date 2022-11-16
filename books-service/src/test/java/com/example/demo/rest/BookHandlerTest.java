package com.example.demo.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.HelperClass;
import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.book.Book;
import com.example.demo.book.BookRepository;
import com.example.demo.book.BookService;
import com.example.demo.book.BookServiceImpl;
import com.example.demo.error.CustomErrorAttributes;
import com.example.demo.error.GlobalErrorWebExceptionHandler;
import com.netflix.discovery.converters.Auto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@WebFluxTest
@Import({BookRoutesV1Config.class, BookHandler.class, BookServiceImpl.class, GlobalErrorWebExceptionHandler.class, CustomErrorAttributes.class, HelperClass.class})
class BookHandlerTest {
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private HelperClass helperClass;

    @MockBean
    private BookRepository bookRepository;

    private List<Book> books = HelperClass.getFakeBooksList();

    @Test
    void getBookByISBN() {
        String isbn = "123456789";
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.just(books.get(0)));

        webClient.get().uri("/api/v1/books/{ISBN}",isbn)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)

                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.author").isEqualTo(books.get(0).getAuthor())
                .jsonPath("$.title").isEqualTo(books.get(0).getTitle())
                .jsonPath("$.isbn").isEqualTo(books.get(0).getISBN())
                .jsonPath("$.pages").isEqualTo(books.get(0).getPages())
                .jsonPath("$.hardTop").isEqualTo(books.get(0).isHardTop())
                .jsonPath("$.ebook").isEqualTo(books.get(0).isEBook())
                .jsonPath("$.imageUrl").isEqualTo(books.get(0).getImageUrl());

        Mockito.verify(bookRepository, times(1)).findByISBN(isbn);
    }

    @Test
    void getBooksWithDefaultPagination() {
        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);
        Mockito.when(bookRepository.findAllBy(pageRequestArgumentCaptor.capture())).thenReturn(Flux.just(books.toArray(new Book[0])));
        Mockito.when(bookRepository.count()).thenReturn(Mono.just((long) books.size()));

        webClient.get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(books.size())  // from userRepository.count()
                .jsonPath("$.number").isEqualTo(pageRequestArgumentCaptor.getValue().getPageNumber())
                .jsonPath("$.size").isEqualTo(pageRequestArgumentCaptor.getValue().getPageSize())
                .jsonPath("$.numberOfElements").isEqualTo(books.size())
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content").isArray();

        Mockito.verify(bookRepository, times(1)).findAllBy(pageRequestArgumentCaptor.capture());
        Mockito.verify(bookRepository, times(1)).count();
    }

    @Test
    void getBooksWithCustomPagination() {
        Mockito.when(bookRepository.findAllBy(any(PageRequest.class))).thenReturn(Flux.just(books.toArray(new Book[0])));
        Mockito.when(bookRepository.count()).thenReturn(Mono.just((long) books.size()));

        int page = 1;
        int booksPerPage = 1;
        String sortByField = "author";
        webClient.get().uri("/api/v1/books?page={page}&size={size}&sort={sortField}", page, booksPerPage, sortByField)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(books.size())  // from userRepository.count()
                .jsonPath("$.number").isEqualTo(page)
                .jsonPath("$.size").isEqualTo(booksPerPage)
                .jsonPath("$.numberOfElements").isEqualTo(books.size())
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content").isArray();

        Mockito.verify(bookRepository, times(1)).findAllBy(any(PageRequest.class));
        Mockito.verify(bookRepository, times(1)).count();
    }

    @Test
    void createBook() {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(books.get(0).getISBN())).thenReturn(Mono.empty());
        Mockito.when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(Mono.just(books.get(0)));

        webClient.post().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+helperClass.generateJWTTokenWithAuthorities("book:create"))
                .body(BodyInserters.fromValue(books.get(0)))
                .exchange()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().value(HttpHeaders.LOCATION, Matchers.containsString("http://localhost:9090/api/v1/books/"))
                .expectStatus().isCreated();

        assertNull(bookArgumentCaptor.getValue().getId());
        assertEquals(books.get(0).getAuthor(), bookArgumentCaptor.getValue().getAuthor());
        assertEquals(books.get(0).getTitle(), bookArgumentCaptor.getValue().getTitle());
        assertEquals(books.get(0).getISBN(), bookArgumentCaptor.getValue().getISBN());
        assertEquals(books.get(0).getImageUrl(), bookArgumentCaptor.getValue().getImageUrl());
        assertEquals(books.get(0).isHardTop(), bookArgumentCaptor.getValue().isHardTop());
        assertEquals(books.get(0).isEBook(), bookArgumentCaptor.getValue().isEBook());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBookNotAuthenticated() {
        webClient.post().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(books.get(0)))
                .exchange()
                .expectStatus().isForbidden();

        Mockito.verify(bookRepository, never()).findByISBN(any(String.class));
        Mockito.verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBookUnauthorized() {
        webClient.post().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+helperClass.generateJWTTokenWithAuthorities("book:read"))
                .body(BodyInserters.fromValue(books.get(0)))
                .exchange()
                .expectStatus().isForbidden();

        Mockito.verify(bookRepository, never()).findByISBN(any(String.class));
        Mockito.verify(bookRepository, never()).save(any(Book.class));
    }


    @Disabled
    @Test
    void updateBookByISBN() {
        String isbn = "123456789";
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(books.get(0).getISBN())).thenReturn(Mono.just(books.get(0)));
        Mockito.when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(Mono.just(books.get(0)));

        Book update = Book.builder().id("abc").title("Update").author("New Author").ISBN("999").build();

        webClient.put().uri("/api/v1/books/{ISBN}", isbn)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+helperClass.generateJWTTokenWithAuthorities("book:update"))
                .body(BodyInserters.fromValue(update))
                .exchange()
                .expectStatus().isNoContent();

        assertEquals(update.getAuthor(), bookArgumentCaptor.getValue().getAuthor());
        assertEquals(update.getTitle(), bookArgumentCaptor.getValue().getTitle());
        assertEquals(update.getPages(), bookArgumentCaptor.getValue().getPages());
        assertEquals(update.getImageUrl(), bookArgumentCaptor.getValue().getImageUrl());
        assertEquals(books.get(0).getId(), bookArgumentCaptor.getValue().getId()); // ISBN & ID doesn't change
        assertEquals(books.get(0).getISBN(), bookArgumentCaptor.getValue().getISBN()); // ISBN & ID doesn't change
        assertEquals(update.isEBook(), bookArgumentCaptor.getValue().isEBook());
        assertEquals(update.isHardTop(), bookArgumentCaptor.getValue().isHardTop());
        System.out.println(bookArgumentCaptor.getValue());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Disabled
    @Test
    void updateBookByISBNBookDoesntExist() {
    }
    @Disabled
    @Test
    void updateBookByISBNNotAuhenticated() {
    }
    @Disabled
    @Test
    void updateBookByISBNUnauthorized() {
    }

    @Disabled
    @Test
    void patchBookByISBN() {
    }
    @Disabled
    @Test
    void patchBookByISBNBookDoesntExist() {}
    @Disabled
    @Test
    void patchBookByISBNNotAuthenticated() {}
    @Disabled
    @Test
    void patchBookByISBNUnauthorized() {}

    @Disabled
    @Test
    void deleteBookByISBN() {
    }
    @Disabled
    @Test
    void deleteBookByISBNBookDoesntExist() {
    }
    @Disabled
    @Test
    void deleteBookByISBNNotAuthenticated() {
    }
    @Disabled
    @Test
    void deleteBookByISBNUnauthorized() {
    }
}