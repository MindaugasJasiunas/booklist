import { Routes } from '@angular/router';
import { AuthGuard } from 'projects/shell-app/src/app/auth/guard/auth.guard';
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
    canActivate: [AuthGuard]
  },
  {
    path: `my-books/wishlist`,
    component: BooksComponent,
    canActivate: [AuthGuard]
  },
];
