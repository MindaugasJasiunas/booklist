import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { ConsoleToggleService } from './console-toggle.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styles: [''],
})
export class AppComponent {
  title = 'shell-app';
  currentYear: number = new Date().getFullYear();
  companyTitle: string = 'Company, Inc';
  appTitle: string = 'Booklist';

  constructor(private router: Router, private service: AuthService, private consoleToggleService: ConsoleToggleService) {
    // disable console logging in production
    consoleToggleService.disableConsoleInProduction();
  }

  logout() {
    this.service.logout();
  }

  searchBooks(text: string) {
    if (typeof text === 'object') return;
    this.router.navigate(['books'], { queryParams: { search: text } });
  }
}
