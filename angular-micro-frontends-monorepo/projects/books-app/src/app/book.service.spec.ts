import { TestBed } from '@angular/core/testing';

import { BookResponse, BookService, BooksResponse } from './book.service';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { Book } from '../model/book.model';

fdescribe('BookService', () => {
  let service: BookService;
  let controller: HttpTestingController; // we need controller to specify test data // Controller to be injected into tests, that allows for mocking and flushing of requests.

  const BOOKS: Book[] = [
    new Book(undefined, "author1", "title1", "abc123", 1, true, false, "https://img1"),
    new Book(undefined, "author2", "title2", "cba321", 1, true, false, "https://img2")
  ];

  const ISBN = 'abc123';
  const BOOK_URL = `http://localhost:9090/api/v1/books/${ISBN}`;
  const BOOKS_URL = 'http://localhost:9090/api/v1/books/';
  const SEARCH_BOOKS_URL = 'http://localhost:9090/api/v1/books?search=';
  const MY_BOOKS_URL = 'http://localhost:9090/api/v1/my-books/';
  const WISHLIST_URL = 'http://localhost:9090/api/v1/wishlist/';
  const DEFAULT_PAGINATION_URL = 'page=0&size=10';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // mock implementation for HttpClient
      providers: [BookService]
    });
    service = TestBed.inject(BookService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get book by ISBN', () => {
    const bookResponse: BookResponse = convertToBookResponse(BOOKS.at(0)!);

    service.getBook(ISBN).subscribe({
      next: (book: Book | undefined) => {
        expect(book).toBeDefined();
        expect(book!.author).toBe(BOOKS.at(0)!.author);
        expect(book!.title).toBe(BOOKS.at(0)!.title);
        expect(book!.ISBN).toBe(ISBN);
        expect(book!.pages).toBe(BOOKS.at(0)!.pages);
        expect(book!.image).toBe(BOOKS.at(0)!.image);
        expect(book!.isEbook).toBe(BOOKS.at(0)!.isEbook);
        expect(book!.isHardtop).toBe(BOOKS.at(0)!.isHardtop);
      },
      error: (err) => fail(err)
    });

    // test mock request
    const request = controller.expectOne(BOOK_URL); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush(bookResponse);
  });

  it('should get books - with default pagination', () => {
    service.getBooks().subscribe({
      next: (books: Book[] | undefined) => {
        expect(books).toBeDefined();
        expect(books!.length).toBe(2);

        expect(books!.at(0)?.title).toBe(BOOKS.at(0)!.title)
        expect(books!.at(0)?.author).toBe(BOOKS.at(0)!.author)
        expect(books!.at(0)?.ISBN).toBe(BOOKS.at(0)!.ISBN)
        expect(books!.at(0)?.image).toBe(BOOKS.at(0)!.image)
        expect(books!.at(0)?.pages).toBe(BOOKS.at(0)!.pages)
        expect(books!.at(0)?.isEbook).toBe(BOOKS.at(0)!.isEbook)
        expect(books!.at(0)?.isHardtop).toBe(BOOKS.at(0)!.isHardtop)

        expect(books!.at(1)?.title).toBe(BOOKS.at(1)!.title)
        expect(books!.at(1)?.author).toBe(BOOKS.at(1)!.author)
        expect(books!.at(1)?.ISBN).toBe(BOOKS.at(1)!.ISBN)
        expect(books!.at(1)?.image).toBe(BOOKS.at(1)!.image)
        expect(books!.at(1)?.pages).toBe(BOOKS.at(1)!.pages)
        expect(books!.at(1)?.isEbook).toBe(BOOKS.at(1)!.isEbook)
        expect(books!.at(1)?.isHardtop).toBe(BOOKS.at(1)!.isHardtop)
      },
      error: (err) => fail(err)
    });

    // test mock request
    const request = controller.expectOne(BOOKS_URL+"?"+DEFAULT_PAGINATION_URL); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: [convertToBookResponse(BOOKS.at(0)!), convertToBookResponse(BOOKS.at(1)!)]}); // same format as original request
  });

  it('should get books - testing custom pagination', () => {
    const page = 3;
    const size = 15;
    service.getBooks(page, size).subscribe();

    // test mock request
    const request = controller.expectOne(BOOKS_URL+`?page=${page}&size=${size}`); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: []}); // same format as original request
  });


  it('should search books - with default pagination', () => {
    const search = 'searchAttribute';
    service.searchBooks(undefined, undefined, search).subscribe({
      next: (books: Book[] | undefined) => {
        expect(books).toBeDefined();
        expect(books!.length).toBe(2);

        expect(books!.at(0)?.title).toBe(BOOKS.at(0)!.title)
        expect(books!.at(0)?.author).toBe(BOOKS.at(0)!.author)
        expect(books!.at(0)?.ISBN).toBe(BOOKS.at(0)!.ISBN)
        expect(books!.at(0)?.image).toBe(BOOKS.at(0)!.image)
        expect(books!.at(0)?.pages).toBe(BOOKS.at(0)!.pages)
        expect(books!.at(0)?.isEbook).toBe(BOOKS.at(0)!.isEbook)
        expect(books!.at(0)?.isHardtop).toBe(BOOKS.at(0)!.isHardtop)

        expect(books!.at(1)?.title).toBe(BOOKS.at(1)!.title)
        expect(books!.at(1)?.author).toBe(BOOKS.at(1)!.author)
        expect(books!.at(1)?.ISBN).toBe(BOOKS.at(1)!.ISBN)
        expect(books!.at(1)?.image).toBe(BOOKS.at(1)!.image)
        expect(books!.at(1)?.pages).toBe(BOOKS.at(1)!.pages)
        expect(books!.at(1)?.isEbook).toBe(BOOKS.at(1)!.isEbook)
        expect(books!.at(1)?.isHardtop).toBe(BOOKS.at(1)!.isHardtop)
      },
      error: (err) => fail(err)
    });

    // test mock request
    const request = controller.expectOne(SEARCH_BOOKS_URL+search+"&"+DEFAULT_PAGINATION_URL); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: [convertToBookResponse(BOOKS.at(0)!), convertToBookResponse(BOOKS.at(1)!)]}); // same format as original request
  })

  it('should search books - custom pagination', () => {
    const page = 3;
    const size = 15;
    const search = 'searchAttribute';
    service.searchBooks(page, size, search).subscribe();

    // test mock request
    const request = controller.expectOne(SEARCH_BOOKS_URL+search+`&page=${page}&size=${size}`); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: []}); // same format as original request
  })

  it('should get My Books - with default pagination', () => {
    service.getMyBooks().subscribe({
      next: (books: Book[] | undefined) => {
        expect(books).toBeDefined();
        expect(books!.length).toBe(2);

        expect(books!.at(0)?.title).toBe(BOOKS.at(0)!.title)
        expect(books!.at(0)?.author).toBe(BOOKS.at(0)!.author)
        expect(books!.at(0)?.ISBN).toBe(BOOKS.at(0)!.ISBN)
        expect(books!.at(0)?.image).toBe(BOOKS.at(0)!.image)
        expect(books!.at(0)?.pages).toBe(BOOKS.at(0)!.pages)
        expect(books!.at(0)?.isEbook).toBe(BOOKS.at(0)!.isEbook)
        expect(books!.at(0)?.isHardtop).toBe(BOOKS.at(0)!.isHardtop)

        expect(books!.at(1)?.title).toBe(BOOKS.at(1)!.title)
        expect(books!.at(1)?.author).toBe(BOOKS.at(1)!.author)
        expect(books!.at(1)?.ISBN).toBe(BOOKS.at(1)!.ISBN)
        expect(books!.at(1)?.image).toBe(BOOKS.at(1)!.image)
        expect(books!.at(1)?.pages).toBe(BOOKS.at(1)!.pages)
        expect(books!.at(1)?.isEbook).toBe(BOOKS.at(1)!.isEbook)
        expect(books!.at(1)?.isHardtop).toBe(BOOKS.at(1)!.isHardtop)
      },
      error: (err) => fail(err)
    });

    // test mock request
    const request = controller.expectOne(MY_BOOKS_URL+"?"+DEFAULT_PAGINATION_URL); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: [convertToBookResponse(BOOKS.at(0)!), convertToBookResponse(BOOKS.at(1)!)]}); // same format as original request
  })

  it('should get My Books - with custom pagination', () => {
    const page = 3;
    const size = 15;
    service.getMyBooks(page, size).subscribe();

    // test mock request
    const request = controller.expectOne(MY_BOOKS_URL+`?page=${page}&size=${size}`); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: []}); // same format as original request
  })

  it('should get Wishlist - with default pagination', () => {
    service.getMyWishlist().subscribe({
      next: (books: Book[] | undefined) => {
        expect(books).toBeDefined();
        expect(books!.length).toBe(2);

        expect(books!.at(0)?.title).toBe(BOOKS.at(0)!.title)
        expect(books!.at(0)?.author).toBe(BOOKS.at(0)!.author)
        expect(books!.at(0)?.ISBN).toBe(BOOKS.at(0)!.ISBN)
        expect(books!.at(0)?.image).toBe(BOOKS.at(0)!.image)
        expect(books!.at(0)?.pages).toBe(BOOKS.at(0)!.pages)
        expect(books!.at(0)?.isEbook).toBe(BOOKS.at(0)!.isEbook)
        expect(books!.at(0)?.isHardtop).toBe(BOOKS.at(0)!.isHardtop)

        expect(books!.at(1)?.title).toBe(BOOKS.at(1)!.title)
        expect(books!.at(1)?.author).toBe(BOOKS.at(1)!.author)
        expect(books!.at(1)?.ISBN).toBe(BOOKS.at(1)!.ISBN)
        expect(books!.at(1)?.image).toBe(BOOKS.at(1)!.image)
        expect(books!.at(1)?.pages).toBe(BOOKS.at(1)!.pages)
        expect(books!.at(1)?.isEbook).toBe(BOOKS.at(1)!.isEbook)
        expect(books!.at(1)?.isHardtop).toBe(BOOKS.at(1)!.isHardtop)
      },
      error: (err) => fail(err)
    });

    // test mock request
    const request = controller.expectOne(WISHLIST_URL+"?"+DEFAULT_PAGINATION_URL); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: [convertToBookResponse(BOOKS.at(0)!), convertToBookResponse(BOOKS.at(1)!)]}); // same format as original request
  })

  it('should get Wishlist - with custom pagination', () => {
    const page = 3;
    const size = 15;
    service.getMyWishlist(page, size).subscribe();

    // test mock request
    const request = controller.expectOne(WISHLIST_URL+`?page=${page}&size=${size}`); // expect one request sent
    expect(request.request.method.toUpperCase()).toEqual("GET"); // expect that request sent is 'GET'
    // specify which data should our mocked HttpClient return
    request.flush({content: []}); // same format as original request
  })

  it('should add(POST) book to My Books', () => {
    const isbn = "abc123";
    service.postToMyBooks(isbn).subscribe();

    // test mock request
    const request = controller.expectOne(MY_BOOKS_URL+isbn);
    expect(request.request.method.toUpperCase()).toEqual("POST");
    // specify which data should our mocked HttpClient return
    request.flush(null, {status: 204, statusText: 'No Content'}) // same format as original request
  })

  it('should remove(DELETE) book from My Books', () => {
    const isbn = "abc123";
    service.deleteFromMyBooks(isbn).subscribe();

    // test mock request
    const request = controller.expectOne(MY_BOOKS_URL+isbn);
    expect(request.request.method.toUpperCase()).toEqual("DELETE");
    // specify which data should our mocked HttpClient return
    request.flush(null, {status: 200, statusText: 'OK'}) // same format as original request
  })

  it('should add(POST) book to Wishlist', () => {
    const isbn = "abc123";
    service.postToWishlist(isbn).subscribe();

    // test mock request
    const request = controller.expectOne(WISHLIST_URL+isbn);
    expect(request.request.method.toUpperCase()).toEqual("POST");
    // specify which data should our mocked HttpClient return
    request.flush(null, {status: 204, statusText: 'No Content'}) // same format as original request
  })

  it('should remove(DELETE) book from Wishlist', () => {
    const isbn = "abc123";
    service.deleteFromWishlist(isbn).subscribe();

    // test mock request
    const request = controller.expectOne(WISHLIST_URL+isbn);
    expect(request.request.method.toUpperCase()).toEqual("DELETE");
    // specify which data should our mocked HttpClient return
    request.flush(null, {status: 200, statusText: 'OK'}) // same format as original request
  })

});

function convertToBookResponse(book: Book): BookResponse{
  const bookResponse: BookResponse = {
    id: undefined,
    author: book.author,
    title: book.title,
    isbn: book.ISBN,
    pages: book.pages,
    hardTop: book.isHardtop,
    ebook: book.isEbook,
    imageUrl: book.image!
  };
  return bookResponse;
}
