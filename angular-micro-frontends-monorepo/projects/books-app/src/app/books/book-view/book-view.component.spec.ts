import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Book } from 'projects/books-app/src/model/book.model';
import { of } from 'rxjs';

import { BookViewComponent } from './book-view.component';

fdescribe('BookViewComponent', () => {
  let component: BookViewComponent;
  let fixture: ComponentFixture<BookViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ BookViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookViewComponent);
    component = fixture.componentInstance;
    // set @Inputs
    component.isLoggedIn = false;
    component.book$ = of(new Book(undefined, "author", "title", "abc123", 1, false, true, "https://img1"));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should NOT show user options when logged-out', () => {
    expect(fixture.debugElement.nativeElement.querySelectorAll('#logged-in-options').length).toBe(0);
  });

  it('should show user options when logged-in', () => {
    component.isLoggedIn = true;
    fixture.detectChanges();

    // test for a logged-in user content to exist
    expect(fixture.debugElement.nativeElement.querySelectorAll('#logged-in-options').length).toBe(1);
    expect(fixture.debugElement.nativeElement.querySelector('#logged-in-options').querySelector('p').textContent).toBe("Logged-in user zone");

    // test buttons clicks
    const addToMyBooksSpy = spyOn(component.addToMyBooks, 'emit');
    const removeFromMyBooksSpy = spyOn(component.removeFromMyBooks, 'emit');
    const addToWishlistSpy = spyOn(component.addToWishlist, 'emit');
    const removeFromWishlistSpy = spyOn(component.removeFromWishlist, 'emit');

    const buttons: NodeList = fixture.debugElement.nativeElement.querySelector('#logged-in-options').querySelectorAll('button');

    buttons.forEach(button => (button as HTMLButtonElement).click());

    expect(addToMyBooksSpy).toHaveBeenCalled();
    expect(removeFromMyBooksSpy).toHaveBeenCalled();
    expect(addToWishlistSpy).toHaveBeenCalled();
    expect(removeFromWishlistSpy).toHaveBeenCalled();
  });
});
