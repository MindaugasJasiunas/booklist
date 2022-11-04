import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BooksComponent } from './books.component';
import { BooksViewComponent } from './books-view/books-view.component';
import { RouterModule } from '@angular/router';
import { MyBooksComponent } from './my-books/my-books.component';
import { MyBooksWishlistComponent } from './my-books-wishlist/my-books-wishlist.component';
import { BOOKS_ROUTES } from './books.routes';
import { BookViewComponent } from './book-view/book-view.component';

@NgModule({
  declarations: [
    BooksComponent,
    BooksViewComponent,
    MyBooksComponent,
    MyBooksWishlistComponent,
    BookViewComponent,
  ],
  imports: [CommonModule, RouterModule, RouterModule.forChild(BOOKS_ROUTES)],
})
export class BooksModule {}
