import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBooksWishlistComponent } from './my-books-wishlist.component';

describe('MyBooksWishlistComponent', () => {
  let component: MyBooksWishlistComponent;
  let fixture: ComponentFixture<MyBooksWishlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyBooksWishlistComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBooksWishlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
