import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Book } from 'projects/books-app/src/model/book.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-books-view',
  templateUrl: './books-view.component.html',
  styleUrls: ['./books-view.component.css'],
})
export class BooksViewComponent implements OnInit {
  @Input() books$!: Observable<Book[] | null>;
  @Input() isSearch!: boolean;
  @Output() showBookInfo = new EventEmitter<string>();

  constructor() {}

  ngOnInit(): void {}

  openBook(ISBN: string) {
    this.showBookInfo.emit(ISBN);
  }
}
