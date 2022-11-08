import { Injectable } from '@angular/core';
import { catchError, EMPTY, filter, map, Observable, of, retry, tap } from 'rxjs';
import { Book } from '../model/book.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  getBookUrl: string = 'http://localhost:9090/api/v1/books'; //environment.getBookUrl;
  getBooksUrl: string = 'http://localhost:9090/api/v1/books/'; //environment.getBooksUrl;
  searchBooksUrl: string = 'http://localhost:9090/api/v1/books?search='; //environment.searchBooksUrl;

  constructor(private http: HttpClient) {}

  getBook(ISBN: string): Observable<Book | undefined> {
    console.log(`[BookService] getBook(ISBN=${ISBN})`);

    return this.http.get(`${this.getBookUrl}/${ISBN}`).pipe(
      retry(5),
      map(obj => obj as BookResponse),
      map(bookResponse => new Book(undefined, bookResponse.author, bookResponse.title, bookResponse.isbn, bookResponse.pages, bookResponse.hardTop, bookResponse.ebook, bookResponse.imageUrl)),
      catchError(() => EMPTY),
    );
  }

  getBooks(page: number = 0, size: number = 10): Observable<Book[]> {
    console.log(`[BookService] getBooks(page=${page}, size=${size})`);

    return this.http.get(this.getBooksUrl).pipe(
      retry(5),
      map(response => (response as BooksResponse).content),
      map((content: []) => content.map(obj => { // map each element & return array to other map
        const bookResponse = obj as BookResponse;
        return new Book(undefined, bookResponse.author, bookResponse.title, bookResponse.isbn, bookResponse.pages, bookResponse.hardTop, bookResponse.ebook, bookResponse.imageUrl);
      })),
      map((books: Book[]) => {
        return books;
      }),
      catchError(() => EMPTY),
    );
  }

  searchBooks(searchText: string, sort?: string): Observable<Book[]> {
    console.log(`[BookService] searchBooks(searchText=${searchText}, sort=${sort})`);
    return this.http.get(this.searchBooksUrl+searchText).pipe(
      map(response => (response as BooksResponse).content),
      map((content: []) => content.map(obj => {
        const bookResponse = obj as BookResponse;
        return new Book(undefined, bookResponse.author, bookResponse.title, bookResponse.isbn, bookResponse.pages, bookResponse.hardTop, bookResponse.ebook, bookResponse.imageUrl);
      }))
    );
  }

  getMyBooks(page: number = 0, size: number = 10): Observable<Book[]>{
    console.log(`[BookService] getMyBooks(page=${page}, size=${size})`);
    // call for my books to backend with JWT, by which books will be found in DB & returned
    return of();
  }

  getMyWishlist(page: number = 0, size: number = 10): Observable<Book[]>{
    console.log(`[BookService] getMyWishlist(page=${page}, size=${size})`);
    // call for my wishlist to backend with JWT, by which books will be found in DB & returned
    return of();
  }
}

export type BookResponse = {
  id: string | undefined,
  author: string,
  title: string,
  isbn: string,
  pages: number,
  hardTop: boolean,
  ebook: boolean,
  imageUrl: string
}

export type BooksResponse= {
  content: [],
  empty: boolean,
  first: boolean,
  last: boolean,
  number: number,
  numberOfElements: number,
  pageable: {
    offset: number,
    pageNumber: number,
    pageSize: number,
    paged: boolean,
    sort: {
      empty: boolean,
      sorted: boolean,
      unsorted: boolean
    },
    unpaged: boolean
  },
  size: number,
  sort: {
    empty :boolean,
    sorted: boolean,
    unsorted: boolean
  },
  totalElements: number
  totalPages: number
}
