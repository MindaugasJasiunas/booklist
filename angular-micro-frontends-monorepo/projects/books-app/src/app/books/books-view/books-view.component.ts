import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Book } from 'projects/books-app/src/model/book.model';
import { Observable } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-books-view',
  imports: [CommonModule],
  templateUrl: './books-view.component.html',
  styleUrls: ['./books-view.component.css'],
})
export class BooksViewComponent implements OnInit {
  @Input() books$!: Observable<Book[] | null>;
  @Input() titleText!: string;
  @Input() noResultsText!: string;
  @Output() showBookInfo = new EventEmitter<string>();

  constructor() {}

  ngOnInit(): void {}

  onOpenBook(ISBN: string) {
    this.showBookInfo.emit(ISBN);
  }
}
