import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {AuthenticationService} from '../../services/authentification.service';
import {Router} from '@angular/router';
import {Page} from '../../model/page.model';
import {Order} from '../../model/order.model';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  constructor(public api: APIService, public authService: AuthenticationService, public router: Router) {
  }

  public size = 8;
  public page = 0;
  public orders: Page<Order>;
  public listSize: Array<number> = [8, 16, 32];

  ngOnInit() {
    this.getOrders(this.page, this.size);
  }

  getOrders(page, size) {
    this.api.getRessources<Page<Order>>('/p12-order/userRole/orders/' +
      this.authService.getUserName() + '/' + page + '/' + size).subscribe(value => {
      this.orders = value;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  onSearchByPhiltre(data) {
    this.size = data.size;
    this.getOrders(this.page, this.size);
  }
}
