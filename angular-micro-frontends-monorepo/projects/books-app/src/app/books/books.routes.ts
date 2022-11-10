import { Routes } from '@angular/router';
import { IsBooklisterGuard } from 'projects/shell-app/src/app/auth/guard/is-booklister.guard';
import { LoggedInGuard } from 'projects/shell-app/src/app/auth/guard/logged-in.guard';
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
    canActivate: [LoggedInGuard, IsBooklisterGuard],
  },
  {
    path: `my-books/wishlist`,
    component: BooksComponent,
    canActivate: [LoggedInGuard, IsBooklisterGuard],
  },
];
