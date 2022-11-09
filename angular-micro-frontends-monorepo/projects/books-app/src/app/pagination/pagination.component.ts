import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  standalone: true,
  selector: 'pagination-component',
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css'],
})
export class PaginationComponent implements OnInit {
  @Input() totalPages$!: Observable<number>;
  @Input() currentPage$!: Observable<number>;
  @Output() prevPage = new EventEmitter<null>();
  @Output() nextPage = new EventEmitter<null>();
  @Output() perPage = new EventEmitter<number>();

  constructor() {}

  ngOnInit(): void {}
}
