import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styles: [''],
})
export class FooterComponent {
  @Input() year!: number;
  @Input() company!: string;
}
