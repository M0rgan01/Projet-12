import {Component, OnInit} from '@angular/core';
import {CaddyService} from '../../services/caddy.service';
import {APIService} from '../../services/api.service';
import {AuthenticationService} from '../../services/authentification.service';
import {Order} from '../../model/order.model';
import {Router} from '@angular/router';
import {OrderProductWrapper} from '../../model/order-product-wrapper.model';


@Component({
  selector: 'app-caddies',
  templateUrl: './caddies.component.html',
  styleUrls: ['./caddies.component.css']
})
export class CaddiesComponent implements OnInit {

  public listDate: Array<Date> = new Array<Date>();
  public date: number;
  public orderProductWrapper: OrderProductWrapper;

  constructor(public caddyService: CaddyService,
              public api: APIService,
              public authenticationService: AuthenticationService,
              public router: Router) {
  }

  ngOnInit() {
    this.caddyService.loadCaddyFromLocalStorage();
    if (this.authenticationService.isAuth()) {
      this.api.getRessources<Array<Date>>('/p12-order/userRole/listDateReception/').subscribe(value => {
        for (const date of value) {
          this.listDate.push(new Date(date));
        }
      });
    }
  }

  onSubmitOrder() {
    this.orderProductWrapper = new OrderProductWrapper(this.caddyService.getOrderProduct());
    this.api.postRessources<Order>('/p12-order/userRole/order/' + this.authenticationService.getUserName() + '/' + this.date,
      this.orderProductWrapper).subscribe(value => {
        this.router.navigateByUrl('/order/' + value.id);
        this.caddyService.removeCaddy();
        this.caddyService.loadCaddyFromLocalStorage();
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

}
