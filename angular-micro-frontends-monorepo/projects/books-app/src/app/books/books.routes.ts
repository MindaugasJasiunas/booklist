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
  {
    path: `my-books/books`,
    component: BooksComponent,
  },
  {
    path: `my-books/wishlist`,
    component: BooksComponent,
  },
];
