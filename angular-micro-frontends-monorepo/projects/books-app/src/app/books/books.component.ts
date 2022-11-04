import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, switchMap, tap } from 'rxjs';
import { Book } from '../../model/book.model';
import { BookService } from '../book.service';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styles: [],
})
export class BooksComponent implements OnInit {
  books$: Observable<Book[] | null>;
  isSearch!: boolean;

  constructor(private route: ActivatedRoute, private service: BookService, private cdr: ChangeDetectorRef, private router: Router) {
    this.books$ = route.queryParams.pipe(
      map((queryParams) => {
        let query: { search: string; order?: string } = {
          search: queryParams['search'],
          order: queryParams['order'],
        };
        return query;
      }),
      switchMap((queryParamObj) => {
        if (queryParamObj.search == null) {
          return service.getBooks();
        } else {
          // Error: Expression has changed after it was checked. Previous value: 'false'. Current value: 'true'.
          this.isSearch = true;

          // use ChangeDetectionRef to solve an error
          cdr.detectChanges();
          // Or use microtask (different event loop cycle (Angular refreshes view on every microtask)) to solve an error
          // setTimeout(()=>{this.isSearch=true;}, 0);
          // Or use macrotask (finished after every event loop)
          // Promise.resolve().then(()=> {this.isSearch=true;});
          return service.searchBooks(queryParamObj.search, queryParamObj.order);
        }
      })
    );

    /*const searchBooks$ = route.queryParams
      .pipe(
        // tap(_ => this.books$ = service.getBooks()), // by default - get books (if no 'search' arg)
        filter((queryParam) => queryParam['search']),
        tap(_ => this.isSearch=true), // expression changed after it was checked
        map((queryParam) => {
          let query: { search: string; order?: string } = {
            search: queryParam['search'],
            order: queryParam['order'],
          };
          return query;
        }),
        switchMap((query: { search: string; order?: string }) => {
          return service.searchBooks(query.search, query.order);
        }),
      );*/
  }

  ngOnInit(): void {}

  loadBooks(page: number = 0, size: number = 10) {
    this.books$ = this.service.getBooks(page, size);
  }

  loadBook(ISBN: string) {
    this.router.navigate(["book", ISBN], {relativeTo: this.route});
  }
}
