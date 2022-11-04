import { Routes } from '@angular/router';
import { BookViewComponent } from './book-view/book-view.component';
import { BooksComponent } from './books.component';
import { MyBooksWishlistComponent } from './my-books-wishlist/my-books-wishlist.component';
import { MyBooksComponent } from './my-books/my-books.component';

export const BOOKS_ROUTES: Routes = [
  {
    path: '',
    component: BooksComponent,
    pathMatch: 'full',
  },
  {
    path: `book/:ISBN`,
    component: BookViewComponent,
  },
  {
    path: `my-books/books`,
    component: MyBooksComponent,
  },
  {
    path: `my-books/wishlist`,
    component: MyBooksWishlistComponent,
  },
];
