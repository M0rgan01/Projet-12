import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {Order} from '../../model/order.model';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentification.service';
import {Product} from '../../model/product.model';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {

  public order: Order;
  public maxHourCancel: number;
  public update: boolean;
  constructor(public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public  authService: AuthenticationService) {
  }

  ngOnInit() {
    const paramId = this.activeRoute.snapshot.paramMap.get('id');
    if (this.authService.isAdmin()) {
      this.getAdminOrder(paramId);
    } else {
      this.getOrder(paramId);
    }
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

  getAdminOrder(id) {
    this.api.getRessources<Order>('/p12-order/adminRole/order/' + id).subscribe(value => {
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
      return true;
    } else {
      return false;
    }
  }

  onCancelOrder() {
    if (confirm('Êtes-vous sûr ?')) {
      this.api.putRessources<Order>('/p12-order/userRole/cancelOrder/' + this.order.id + '/' + this.authService.getUserName(),
        null).subscribe(value => {
        this.order.cancel = true;
        this.update = true;
      }, error1 => {
        this.router.navigateByUrl('/error');
      });
    }
  }

  onCancelOrderAdmin() {
    if (confirm('Êtes-vous sûr ?')) {
      this.api.putRessources<Order>('/p12-order/adminRole/cancelOrder/' + this.order.id,
        null).subscribe(value => {
        this.order.cancel = true;
        this.update = true;
      }, error1 => {
        this.router.navigateByUrl('/error');
      });
    }
  }

  onPaidOrder() {
    if (confirm('Êtes-vous sûr ?')) {
      this.api.putRessources<Order>('/p12-order/adminRole/paidOrder/' + this.order.id,
        null).subscribe(value => {
        this.order.paid = true;
        this.update = true;
      }, error1 => {
        this.router.navigateByUrl('/error');
      });
    }
  }
}
