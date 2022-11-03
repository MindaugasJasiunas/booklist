import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shell-app';
  currentYear: number = new Date().getFullYear();
  companyTitle: string = 'Company, Inc';
  appTitle: string = 'Booklist';

  logout(){
    console.log('logout');
  }

}
