import {Component, OnInit} from '@angular/core';
import {CaddyService} from '../../services/caddy.service';
import {Caddy} from '../../model/caddy.model';
import {APIService} from '../../services/api.service';
import {OrderProduct} from '../../model/order-product.model';

@Component({
  selector: 'app-caddies',
  templateUrl: './caddies.component.html',
  styleUrls: ['./caddies.component.css']
})
export class CaddiesComponent implements OnInit {

  listDate: Array<Date> = new Array<Date>();
  date;

  constructor(public caddyService: CaddyService, public api: APIService) {
  }

  ngOnInit() {
    this.caddyService.loadCaddyFromLocalStorage();
    this.api.getRessources<Array<Date>>('/p12-order/userRole/listDateReception/').subscribe(value => {
      for (const date of value) {
        this.listDate.push(new Date(date));
      }
    });
  }


  onSubmitOrder(dataForm) {
   /* this.api.postRessources<Array<OrderProduct>>('/p12-order/userRole/order/' + this.caddyService.caddy.userName + '/' + new Date(),
      this.caddyService.caddy).subscribe();*/
   console.log(dataForm);
  }

}
