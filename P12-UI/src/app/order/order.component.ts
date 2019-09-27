import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {Order} from '../../model/order.model';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentification.service';
import {Product} from '../../model/product.model';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {

  public order: Order;
  public maxHourCancel: number;
  public cancel: boolean;
  constructor(public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public  authService: AuthenticationService) {
  }

  ngOnInit() {
    const paramId = this.activeRoute.snapshot.paramMap.get('id');
    this.getOrder(paramId);
    this.getMaxHourCancel();
  }


  getOrder(id) {
    this.api.getRessources<Order>('/p12-order/userRole/order/' + id + '/' + this.authService.getUserName()).subscribe(value => {
      for (const orderProduct of value.orderProducts) {
        this.api.getRessources<Product>('/p12-stock/public/product/' + orderProduct.productId).subscribe(value2 => {
          orderProduct.product = value2;
        }, error1 => {
          this.router.navigateByUrl('/error');
        });
      }
      this.order = value;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  getMaxHourCancel() {
    this.api.getRessources<number>('/p12-order/public/maxHoursCancelOrder').subscribe(value => {
      this.maxHourCancel = value;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  checkCancelOrder(): boolean {
    const date: Date = new Date();
    const dateAfterAddMaxHours = new Date(this.order.date);
    dateAfterAddMaxHours.setHours(dateAfterAddMaxHours.getHours() + this.maxHourCancel);
    if (date.getTime() < dateAfterAddMaxHours.getTime()) {
      console.log('true');
      return true;
    } else {
      console.log('false');
      return false;
    }
  }

  onCancelOrder() {
    this.api.putRessources<Order>('/p12-order/userRole/cancelOrder/' + this.order.id + '/' + this.authService.getUserName(),
      null).subscribe(value => {
      this.order.cancel = true;
      this.cancel = true;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }
}
