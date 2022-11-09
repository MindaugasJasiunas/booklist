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
export class BooksViewComponent {
  @Input() books$!: Observable<Book[] | null>;
  @Input() titleText!: string;
  @Input() noResultsText!: string;
  @Input() currentPage$!: Observable<number>;
  @Output() showBookInfo = new EventEmitter<string>();
  @Output() prevPage = new EventEmitter<null>();
  @Output() nextPage = new EventEmitter<null>();
  @Output() perPage = new EventEmitter<number>();

  onOpenBook(ISBN: string) {
    this.showBookInfo.emit(ISBN);
  }

  loadPrev(){
    this.prevPage.emit();
  }

  loadNext(){
    this.nextPage.emit();
  }

  onPerPage(num: number){
    this.perPage.emit(num);
  }
}
