import {Injectable} from '@angular/core';

@Injectable()
export class PaginationService {

  public startPage: number;
  public endPage: number;
  public numberOfButton = 6;
  public items = [];

  getNumberOfButton(totalPage: number, actual: number) {
    this.items = new Array();

    const halfPagesToShow: number = this.numberOfButton / 2;
    if (totalPage <= this.numberOfButton) {
      this.startPage = 0;
      this.endPage = totalPage;

    } else if (actual - halfPagesToShow <= 0) {
      this.startPage = 0;
      this.endPage = this.numberOfButton;

    } else if (actual + halfPagesToShow === totalPage) {
      this.startPage = (actual - halfPagesToShow);
      this.endPage = totalPage;

    } else if (actual + halfPagesToShow > totalPage) {
      this.startPage = (totalPage - this.numberOfButton);
      this.endPage = totalPage;

    } else {
      this.startPage = (actual - halfPagesToShow);
      this.endPage = (actual + halfPagesToShow);
    }

    for (let i = this.startPage; i < this.endPage; i++) {
      this.items.push(i);
    }

    return this.items;
  }
}
