package com.example.demo.book;

import com.example.demo.HelperClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookServiceImpl bookService;

    List<Book> books = HelperClass.getFakeBooksList();

    @Test
    void getBook() {
        final ArgumentCaptor<String> stringArgumentCaptor=ArgumentCaptor.forClass(String.class);
        Mockito.when(bookRepository.findByISBN(stringArgumentCaptor.capture())).thenReturn(Mono.just(books.get(0)));
        String ISBN = "123456789";
        Book returnedBook = bookService.getBook(ISBN).block();
        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        assertEquals(ISBN, stringArgumentCaptor.getValue());
        assertEquals(books.get(0), returnedBook);
    }

    @Test
    void getBookDoesntExist() {
        String ISBN = "123456789";
        Mockito.when(bookRepository.findByISBN(ISBN)).thenReturn(Mono.empty());
        Book returnedBook = bookService.getBook(ISBN).block();
        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        assertNull(returnedBook);
    }

    @Test
    void getBooks() {
        Mockito.when(bookRepository.findAllBy(any(PageRequest.class))).thenReturn(Flux.just(books.toArray(new Book[0])));
        Mockito.when(bookRepository.count()).thenReturn(Mono.just((long) books.size()));
        int page = 0;
        int sizePerPage = 10;
        PageRequest pageRequest = PageRequest.of(page,sizePerPage, Sort.by("title"));

        Page<Book> booksReturned = bookService.getBooks(pageRequest).block();

        assertEquals(books.size(), booksReturned.getTotalElements());
        assertEquals(books.size()/sizePerPage + (books.size() % sizePerPage !=0 ? 1 : 0), booksReturned.getTotalPages());
        assertEquals(books.get(0), booksReturned.get().toList().get(0));
        assertEquals(books.get(1), booksReturned.get().toList().get(1));

        Mockito.verify(bookRepository, times(1)).count();
        Mockito.verify(bookRepository, times(1)).findAllBy(any(PageRequest.class));
    }

    @Test
    void createBook() {
        Mockito.when(bookRepository.findByISBN(books.get(0).getISBN())).thenReturn(Mono.empty());
        Mockito.when(bookRepository.save(books.get(0))).thenReturn(Mono.just(books.get(0)));

        bookService.createBook(books.get(0)).block();

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("createBook() - book already exists by ISBN. throws runtime error.")
    @Test
    void createBookAlreadyExistsByIsbn() {
        Mockito.when(bookRepository.findByISBN(books.get(0).getISBN())).thenReturn(Mono.just(books.get(0)));

        assertThrows(RuntimeException.class, ()-> bookService.createBook(books.get(0)).block());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, Mockito.never()).save(any(Book.class));
    }

    @Test
    void updateBook() {
        String isbn = "123456789";
        final ArgumentCaptor<Book> bookArgumentCaptor=ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.just(books.get(0)));
        Mockito.when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(Mono.just(books.get(0)));

        Book updatedBook = bookService.updateBook(books.get(1), isbn).block();

        assertEquals(books.get(0), updatedBook);
        assertEquals(books.get(0).getId(), bookArgumentCaptor.getValue().getId());
        assertEquals(books.get(0).getISBN(), bookArgumentCaptor.getValue().getISBN());
        assertEquals(books.get(1).getTitle(), bookArgumentCaptor.getValue().getTitle());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("updateBook() - book doesn't exist - throws runtime error")
    @Test
    void updateBookDoesntExist() {
        String isbn = "123456789";
        final ArgumentCaptor<Book> bookArgumentCaptor=ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.empty());

        assertThrows(RuntimeException.class, ()-> bookService.updateBook(books.get(1), isbn).block());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void patchBook() {
        String isbn = "123456789";
        final ArgumentCaptor<Book> bookArgumentCaptor=ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.just(books.get(0)));
        Mockito.when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(Mono.just(books.get(0)));
        Book patch= Book.builder().author("New Author").title("New Title").build();

        Book patchedBook = bookService.patchBook(patch, isbn).block();

        assertEquals(books.get(0), patchedBook);
        assertEquals(books.get(0).getId(), bookArgumentCaptor.getValue().getId());
        assertEquals(books.get(0).getISBN(), bookArgumentCaptor.getValue().getISBN());
        assertEquals(patch.getTitle(), bookArgumentCaptor.getValue().getTitle());
        assertEquals(patch.getAuthor(), bookArgumentCaptor.getValue().getAuthor());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("patchBook() - book doesn't exist - throws runtime error")
    @Test
    void patchBookDoesntExist() {
        String isbn = "123456789";
        final ArgumentCaptor<Book> bookArgumentCaptor=ArgumentCaptor.forClass(Book.class);
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.empty());

        assertThrows(RuntimeException.class, ()-> bookService.patchBook(books.get(0), isbn).block());

        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
        Mockito.verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook() {
        String isbn = "123456789";
        Mockito.when(bookRepository.deleteByISBN(isbn)).thenReturn(Mono.empty().then()); // return Mono<Void>
        bookService.deleteBook(isbn).block();
        Mockito.verify(bookRepository, times(1)).deleteByISBN(any(String.class));
    }

    @DisplayName("deleteBook() - not existing. still doesn't return anything")
    @Test
    void deleteBookNotExisting() {
        String isbn = "123456789";
        Mockito.when(bookRepository.deleteByISBN(isbn)).thenReturn(Mono.empty().then()); // return Mono<Void>
        bookService.deleteBook(isbn).block();
        Mockito.verify(bookRepository, times(1)).deleteByISBN(any(String.class));
    }

    @Test
    void bookNotExistsInDBByISBNExists() {
        String isbn = "123456";
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.just(books.get(0)));
        assertFalse(bookService.bookNotExistsInDBByISBN(isbn).block(), "false because book exists");
        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
    }

    @Test
    void bookNotExistsInDBByISBNNotExists() {
        String isbn = "123456";
        Mockito.when(bookRepository.findByISBN(isbn)).thenReturn(Mono.empty());
        assertTrue(bookService.bookNotExistsInDBByISBN(isbn).block(), "true because book not exists");
        Mockito.verify(bookRepository, times(1)).findByISBN(any(String.class));
    }
}