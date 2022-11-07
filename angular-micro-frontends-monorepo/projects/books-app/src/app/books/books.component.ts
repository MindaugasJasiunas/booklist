import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { filter, map, Observable, of, switchMap, tap } from 'rxjs';
import { Book } from '../../model/book.model';
import { BookService } from '../book.service';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styles: [],
})
export class BooksComponent implements OnInit {
  book$!: Observable<Book | undefined>;
  books$!: Observable<Book[] | null>;
  _titleText!: BookPageTitle;
  _noResultsText!: NoBookResultText;
  type: BookType = 'books';
  booksView = true;

  constructor(private route: ActivatedRoute, private service: BookService, private cdr: ChangeDetectorRef, private router: Router) {

    if(router.url.includes('/books/book/')) {
      this.booksView = false;
      this.book$ = route.params.pipe(
        filter((param: Params) => param['ISBN']),
        map((param: Params) => param['ISBN']),
        switchMap((isbn: string) => {
          return this.loadBook(isbn);
        })
      );
    }
    else if(router.url.includes('/my-books/books')) {
      this.type = 'my-books';
      this.books$ = this.loadBooks();
    }
    else if(router.url.includes('/my-books/wishlist')) {
      this.type = 'wishlist';
      this.books$ = this.loadBooks();
    }
    else if(router.url.includes('/books') && !router.url.includes('/books/book') || router.url.includes('?search=')) {
      // checks if 'search' query param exists or return all books
      this.books$ = route.queryParams.pipe(
        map((queryParams) => {
          let query: { search: string; order?: string } = {
            search: queryParams['search'],
            order: queryParams['order'],
          };
          return query;
        }),
        switchMap((queryParamObj) => {
          if (queryParamObj.search == null) {
            this.type = 'books';
            return this.loadBooks();
          } else {
            this.type = 'books-search'
            // Error: Expression has changed after it was checked. Previous value: 'false'. Current value: 'true'.

            // use ChangeDetectionRef to solve an error
            cdr.detectChanges();
            // Or use microtask (different event loop cycle (Angular refreshes view on every microtask) to solve an error
            // setTimeout(()=>{this.isSearch=true;}, 0);
            // Or use macrotask (finished after every event loop)
            // Promise.resolve().then(()=> {this.isSearch=true;});
            return this.loadBooks(undefined, undefined, queryParamObj.search, queryParamObj.order);
          }
        })
      );
    }
  }

  ngOnInit(): void {}

  loadBooks(page: number = 0, size: number = 10, search?: string, order?: string): Observable<Book[]> {
    switch(this.type){
      case 'books': return  this.service.getBooks(page, size);
      case 'my-books': return this.service.getMyBooks(page, size);
      case 'wishlist': return this.service.getMyWishlist(page, size);
      case 'books-search': return this.service.searchBooks(search!, order);
      default: return this.service.getBooks(page, size);
    }
  }

  viewBook(ISBN: string){
    this.router.navigate(["books", "book", ISBN]);
  }

  loadBook(ISBN: string) {
    return this.service.getBook(ISBN);
  }

  onAddToMyBooks(isbn: string){
  }

  onRemoveFromMyBooks(isbn: string){
  }

  onAddToWishlist(isbn: string){
  }

  onRemoveFromWishlist(isbn: string){
  }

  get titleText(): BookPageTitle{
    switch(this.type){
      case 'books': return 'Books';
      case 'books-search': return 'Search Books';
      case 'my-books': return 'My Books';
      case 'wishlist': return 'My Wishlist';
    }
  }

  get noResultsText(): NoBookResultText{
    return this.type === 'books-search' ? 'Sorry there is no books matching your search criteria' : 'Sorry there is no books';
  }

}

export type BookPageTitle = 'Books' | 'Search Books' | 'My Books' | 'My Wishlist';
export type BookType = 'books' | 'books-search' | 'my-books' | 'wishlist';
export type NoBookResultText = 'Sorry there is no books matching your search criteria' | 'Sorry there is no books';
