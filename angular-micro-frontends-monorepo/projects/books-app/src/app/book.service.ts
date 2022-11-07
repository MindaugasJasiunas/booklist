import { Injectable } from '@angular/core';
import { filter, map, Observable, of } from 'rxjs';
import { Book } from '../model/book.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  getBookUrl: string = ''; //environment.getBookUrl;
  getBooksUrl: string = ''; //environment.getBooksUrl;
  searchBooksUrl: string = ''; //environment.searchBooksUrl;

  booksList$: Observable<Book[]> = of([
    new Book(1, 'john doe', 'getBooks called title', 'adgssfg685153165', 701, true, false, 'https://t4.ftcdn.net/jpg/00/89/55/15/360_F_89551596_LdHAZRwz3i4EM4J0NHNHy2hEUYDfXc0j.jpg'),
    new Book(2, 'Bono', 'Surrender: 40 Songs, One Story', '0593663691', 240, false, true, 'https://townsquare.media/site/838/files/2022/10/attachment-bono-surrender-book.jpg'),
    new Book(3, 'Matthew Perry', 'Friends, Lovers, and the Big Terrible Thing: A Memoir', '1250866448', 701, true, false, 'https://m.media-amazon.com/images/I/413MbCa36bL.jpg'),
    new Book(4, 'Prince Harry The Duke of Sussex', 'Spare', '0593593804', 240, false, true, 'https://m.media-amazon.com/images/I/91Szm8FpdfL.jpg'),
    new Book(5, 'James Clear', 'Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones', '0735211299', 240, false, true, 'https://m.media-amazon.com/images/I/51-uspgqWIL._AC_SY780_.jpg'),
    new Book(6, 'Robert Greene', 'The 48 Laws of Power', '0140280197', 240, false, true, 'https://thumb.knygos-static.lt/XkcFu0tIV-uiy_uRVJtVs8xYxPY=/fit-in/800x800/filters:cwatermark(static/wm.png,500,75,30)/images/books/1209054/s-l1600.jpg'),
    new Book(7, 'Mark Manson', 'The Subtle Art of Not Giving a F*ck: A Counterintuitive Approach to Living a Good Life', '0062457713', 240, false, true, 'https://thumb.knygos-static.lt/l5QsjoL9u349RVZyEPnU45AdwG0=/fit-in/800x800/filters:cwatermark(static/wm.png,500,75,30)/images/books/1167075/9780062641540.jpg'),
    new Book(8, 'Robert Greene', 'Mastery', '014312417X', 240, true, false, 'https://upload.wikimedia.org/wikipedia/en/9/98/Mastery_Cover.jpg'),
    new Book(9, 'Kaira Rouda', 'The Widow', '1542039215', 240, false, true, 'https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1647428623l/58562607._SY475_.jpg'),
    new Book(10, 'Leah Vernon', 'The Union', '1662500351', 240, true, false, 'https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1649490435l/59785894.jpg'),
    new Book(11, 'Geraldine Brooks', 'Horse: A Novel', '1969215351', 240, true, false, 'https://hachette.imgix.net/books/9780733639678.jpg?auto=compress'),
    new Book(12, 'Jennifer L. Armentrout', 'A Light in the Flame: A Flesh and Fire Novel', '1957568143', 240, true, false, 'https://m.media-amazon.com/images/I/41vN-kXzEpL._AC_SY780_.jpg'),
  ]);

  constructor(private http: HttpClient) {}

  getBook(ISBN: string): Observable<Book | undefined> {
    console.log(`[BookService] getBook(ISBN=${ISBN})`);
    // return this.http.get(this.getBookUrl).pipe();
    return this.booksList$.pipe(
      map((books: Book[]) => books.find(book => book.ISBN === ISBN))
    );
  }

  getBooks(page: number = 0, size: number = 10): Observable<Book[]> {
    console.log(`[BookService] getBooks(page=${page}, size=${size})`);
    // return this.http.get(this.getBooksUrl).pipe();
    return this.booksList$;
  }

  searchBooks(searchText: string, sort?: string): Observable<Book[]> {
    console.log(`[BookService] searchBooks(searchText=${searchText}, sort=${sort})`);
    // check if desc or asc - default asc
    // return this.http.get(this.searchBooksUrl);//.pipe();
    return this.booksList$;
  }

  getMyBooks(page: number = 0, size: number = 10): Observable<Book[]>{
    console.log(`[BookService] getMyBooks(page=${page}, size=${size})`);
    // call for my books to backend with JWT, by which books will be found in DB & returned
    return this.booksList$;
  }

  getMyWishlist(page: number = 0, size: number = 10): Observable<Book[]>{
    console.log(`[BookService] getMyWishlist(page=${page}, size=${size})`);
    // call for my wishlist to backend with JWT, by which books will be found in DB & returned
    return this.booksList$;
  }
}
