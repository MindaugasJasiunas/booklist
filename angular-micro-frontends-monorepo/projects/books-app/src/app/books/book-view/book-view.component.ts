import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Book } from 'projects/books-app/src/model/book.model';
import { filter, map, Observable, switchMap, tap } from 'rxjs';
import { BookService } from '../../book.service';

@Component({
  selector: 'app-book-view',
  templateUrl: './book-view.component.html',
  styleUrls: ['./book-view.component.css'],
})
export class BookViewComponent implements OnInit {
  book$!: Observable<Book | undefined>;

  constructor(private route: ActivatedRoute, private service: BookService) {
    this.book$ = route.params.pipe(
      filter((param: Params) => param['ISBN']),
      map((param: Params) => param['ISBN']),
      switchMap((isbn: string) => {
        return service.getBook(isbn);
      })
    );
  }

  ngOnInit(): void {}
}
