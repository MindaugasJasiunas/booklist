import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BooksComponent } from './books.component';
import { BooksViewComponent } from './books-view/books-view.component';
import { RouterModule } from '@angular/router';
import { BOOKS_ROUTES } from './books.routes';
import { BookViewComponent } from './book-view/book-view.component';

@NgModule({
  declarations: [
    BooksComponent,
    BooksViewComponent,
    BookViewComponent,
  ],
  imports: [CommonModule, RouterModule, RouterModule.forChild(BOOKS_ROUTES)],
})
export class BooksModule {}
