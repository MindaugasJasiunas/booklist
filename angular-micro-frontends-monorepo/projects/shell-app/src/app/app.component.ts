import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'shell-app';
  currentYear: number = new Date().getFullYear();
  companyTitle: string = 'Company, Inc';
  appTitle: string = 'Booklist';

  constructor(private router: Router) {}

  logout() {
    console.log('logout');
  }

  searchBooks(text: string) {
    if (typeof text === 'object') return;
    this.router.navigate(['books'], { queryParams: { search: text } });
  }
}
