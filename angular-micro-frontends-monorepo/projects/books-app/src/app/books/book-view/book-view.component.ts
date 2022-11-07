import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Book } from 'projects/books-app/src/model/book.model';
import { filter, map, Observable, switchMap, tap } from 'rxjs';
import { BookService } from '../../book.service';

@Component({
  standalone: true,
  selector: 'app-book-view',
  imports: [CommonModule],
  templateUrl: './book-view.component.html',
  styleUrls: ['./book-view.component.css'],
})
export class BookViewComponent implements OnInit {
  @Input() book$!: Observable<Book | undefined>;
  @Output() addToMyBooks = new EventEmitter<string>();
  @Output() addToWishlist = new EventEmitter<string>();
  @Output() removeFromMyBooks = new EventEmitter<string>();
  @Output() removeFromWishlist = new EventEmitter<string>();

  constructor() {}

  ngOnInit(): void {}

  onAddToMyBooks(ISBN: string) {
    this.addToMyBooks.emit(ISBN);
  }

  onAddToWishlist(ISBN: string) {
    this.addToWishlist.emit(ISBN);
  }

  onRemoveFromMyBooks(ISBN: string) {
    this.removeFromMyBooks.emit(ISBN);
  }

  onRemoveFromWishlist(ISBN: string) {
    this.removeFromWishlist.emit(ISBN);
  }
}
