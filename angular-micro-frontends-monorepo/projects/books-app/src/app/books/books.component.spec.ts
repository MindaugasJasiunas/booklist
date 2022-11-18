import { ChangeDetectorRef } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'projects/shell-app/src/app/auth/auth.service';
import { BehaviorSubject, combineLatest, of, switchMap } from 'rxjs';
import { Book } from '../../model/book.model';
import { BookService } from '../book.service';

import { BooksComponent } from './books.component';

fdescribe('BooksComponent', () => {
  let component: BooksComponent;
  let fixture: ComponentFixture<BooksComponent>;

  let bookServiceStub: any;

  beforeEach(async () => {
    // mocking services
    bookServiceStub = {
      totalBooks: new BehaviorSubject(0),
      getBook: (ISBN: number) => of(
        new Book(undefined, "author1", "get-book", "abc123",ISBN, true, false, "http://img1")
      ),
      getBooks: (page: number, size: number) => of([
        new Book(undefined, "author1", "get-books", "abc123",123, true, false, "http://img1"),
        new Book(undefined, "author2", "get-books", "cba321",321, true, false, "http://img2"),
      ]),
      getMyBooks: (page: number, size: number) => of([
        new Book(undefined, "author1", "get-my-books", "abc123",123, true, false, "http://img1"),
        new Book(undefined, "author2", "get-my-books", "cba321",321, true, false, "http://img2"),
      ]),
      getMyWishlist: (page: number, size: number) => of([
        new Book(undefined, "author1", "get-my-wishlist", "abc123",123, true, false, "http://img1"),
        new Book(undefined, "author2", "get-my-wishlist", "cba321",321, true, false, "http://img2"),
      ]),
      searchBooks: (page: number, size: number, search: string, order: string | undefined) => of([
        new Book(undefined, "author1", "search-books", "abc123",123, true, false, "http://img1"),
        new Book(undefined, "author2", "search-books", "cba321",321, true, false, "http://img2"),
      ]),
    };

    await TestBed.configureTestingModule({
      declarations: [BooksComponent],
      providers: [
        { provide: BookService, useValue: bookServiceStub },
        { provide: AuthService,
          useValue: {
            isLoggedIn: () => false
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            params: {}
          },
        },
        {
          provide: Router,
          useValue: {
            url: '/books/book',
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should be 2 total pages', waitForAsync(() => {
    const service = TestBed.inject(BookService);
    component.itemsPerPage$ = new BehaviorSubject(10);
    service.totalBooks = new BehaviorSubject(15);

    fixture = TestBed.createComponent(BooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    combineLatest([service.totalBooks, component.itemsPerPage$, component.totalPages$]).pipe(
      switchMap(([booksTotal, booksPerPage, totalPages]) => {
        expect(totalPages).toBe(Math.ceil(booksTotal/booksPerPage));
        return of();
      }
    )).subscribe();
  }));

  it('should load my-books', waitForAsync(() => {
    const router = TestBed.inject(Router); // inject mocked router
    // @ts-ignore: force this private property value for testing.
    router.url = "/my-books/books";
    expect(router.url).toContain('/my-books/books');

    fixture = TestBed.createComponent(BooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.type).toBe('my-books');

    // test books$
    component.books$.subscribe(books => {
      expect(books!.length).toBe(2);
      books!.every(book => expect(book.title).toBe('get-my-books'));
    })

    // test HTML template to load app-book-view and not app-books-view component
    expect(fixture.debugElement.nativeElement.querySelector('app-books-view')).toBeDefined();
    expect(fixture.debugElement.nativeElement.querySelector('app-book-view')).toBeNull();
  }));

  it('should load wishlist', () => {
    const router = TestBed.inject(Router);
    // @ts-ignore: force this private property value for testing.
    router.url = "/my-books/wishlist";
    expect(router.url).toContain('/my-books/wishlist');

    fixture = TestBed.createComponent(BooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.type).toBe('wishlist');

    // test books$
    component.books$.subscribe(books => {
      expect(books!.length).toBe(2);
      books!.every(book => expect(book.title).toBe('get-my-wishlist'));
    })

    // test HTML template to load app-book-view and not app-books-view component
    expect(fixture.debugElement.nativeElement.querySelector('app-books-view')).toBeDefined();
    expect(fixture.debugElement.nativeElement.querySelector('app-book-view')).toBeNull();
  });

  it('should load book', waitForAsync(() => {
    const router = TestBed.inject(Router);
    const route = TestBed.inject(ActivatedRoute);
    const isbn = 'abc123';
    // @ts-ignore: force this private property value for testing.
    router.url = "/books/book/"+isbn;
    route.params = of({'ISBN': isbn});
    expect(router.url).toContain('/books/book/'+isbn);

    fixture = TestBed.createComponent(BooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.type).toBe('book');
    expect(component.booksView).toBeFalse();

    // test book$
    component.book$.subscribe(book => {
      expect(book!.title).toBe('get-book');
      expect(book!.ISBN).toBe(isbn);
    });

    // test HTML template to load app-book-view and not app-books-view component
    expect(fixture.debugElement.nativeElement.querySelector('app-book-view')).toBeDefined();
    expect(fixture.debugElement.nativeElement.querySelector('app-books-view')).toBeNull();
  }));


});
