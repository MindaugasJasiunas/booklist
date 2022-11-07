import { Routes } from '@angular/router';
import { BookViewComponent } from './book-view/book-view.component';
import { BooksComponent } from './books.component';

export const BOOKS_ROUTES: Routes = [
  {
    path: '',
    component: BooksComponent,
    pathMatch: 'full',
  },
  {
    path: `book/:ISBN`,
    component: BooksComponent,
  },
  // instead of this routes - use re-same BooksComponent & based on URL - load stuff
  // {
  //   path: `book/:ISBN`,
  //   component: BookViewComponent,
  // },
  // {
  //   path: `my-books/books`,
  //   component: MyBooksComponent,
  // },
  // {
  //   path: `my-books/wishlist`,
  //   component: MyBooksWishlistComponent,
  // },
  {
    path: `my-books/books`,
    component: BooksComponent,
  },
  {
    path: `my-books/wishlist`,
    component: BooksComponent,
  },
];
