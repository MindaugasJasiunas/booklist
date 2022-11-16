package com.example.demo.rest;

import com.example.demo.HelperClass;
import com.example.demo.book.Book;
import com.example.demo.book.BookRepository;
import com.example.demo.book.BookService;
import com.example.demo.book.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@Import({BookRoutesV1Config.class, BookHandler.class, BookServiceImpl.class})
class BookHandlerTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    BookService bookService;

    List<Book> books = HelperClass.getFakeBooksList();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getBookByISBN() {

        String isbn = "123456789";
        Mockito.when(bookService.getBook(isbn)).thenReturn(Mono.just(books.get(0)));

        webClient.get().uri("/api/v1/books/{ISBN}",isbn)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Book.class);
//                .jsonPath("$.totalElements").isEqualTo(10)  // from userRepository.count()
//                .jsonPath("$.number").isEqualTo(0)  // default page number - 0
//                .jsonPath("$.size").isEqualTo(5)  // default items per page - 5
//                .jsonPath("$.numberOfElements").isEqualTo(getPackages().size())
//                .jsonPath("$.content").isNotEmpty()
//                .jsonPath("$.content").isArray();
    }

    @Test
    void getBooks() {
    }

    @Test
    void createBook() {
    }

    @Test
    void updateBookByISBN() {
    }

    @Test
    void patchBookByISBN() {
    }

    @Test
    void deleteBookByISBN() {
    }

    private List<Book> getFakeBooksList(){
        return List.of(
            Book.builder().id("abc").title("Title1").author("Author1").ISBN("123456789").pages(123).imageUrl("https://img1").isEBook(true).isHardTop(false).build(),
            Book.builder().id("def").title("Title2").author("Author2").ISBN("987654321").pages(321).imageUrl("https://img2").isEBook(false).isHardTop(true).build()
        );
    }
}