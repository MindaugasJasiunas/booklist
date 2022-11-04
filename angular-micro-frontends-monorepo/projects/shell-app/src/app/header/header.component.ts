import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  @Input() title!: string;
  @Output() logout = new EventEmitter<void>();
  @Output() search = new EventEmitter<string>();
  @ViewChild("searchForm", { static: true }) form!: NgForm;

  onSearch(){
    this.search.emit( (this.form.value as {search: string}).search );
  }
  
}
