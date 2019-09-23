import {Injectable} from '@angular/core';
import {Caddy} from '../model/caddy.model';
import {Product} from '../model/product.model';
import {OrderProduct} from '../model/order-product.model';
import {AuthenticationService} from './authentification.service';

@Injectable()
export class CaddyService {

  public caddy;

  constructor(public authenticationService: AuthenticationService) {
    this.loadCaddyFromLocalStorage();
  }

  loadCaddyFromLocalStorage() {
    const caddies = localStorage.getItem('myCaddies');
    if (caddies) {
      console.log('chargement du caddie');
      this.caddy = JSON.parse(caddies);
    } else {
      console.log('nouveau caddy');
      this.caddy = new Caddy();
    }
  }

  saveCaddies() {
    // on enregistre les caddies au format JSON
    localStorage.setItem('myCaddies', JSON.stringify(this.caddy));
  }

  addProductToCaddy(product: Product) {

    if (this.getSize() === 0) {
      this.caddy.userName = this.authenticationService.getUserName();
    }

    let orderProduct = this.caddy.items[product.id];
    console.log(product.orderQuantity);
    if (orderProduct === undefined) {
      orderProduct = new OrderProduct();
      orderProduct.productId = product.id;
      orderProduct.product = product;
      orderProduct.orderQuantity = product.orderQuantity;
      orderProduct.totalPriceRow = this.getTotalPriceRow(orderProduct);
      this.caddy.items[product.id] = orderProduct;
    } else {
      orderProduct.orderQuantity += product.orderQuantity;
      orderProduct.totalPriceRow = this.getTotalPriceRow(orderProduct);
      this.caddy.items[product.id] = orderProduct;
    }
    this.saveCaddies();
  }


  removeProductToCaddy(product: Product) {
    if (this.caddy.items[product.id]) {
      delete this.caddy.items[product.id];
      this.saveCaddies();
    }
  }

  getTotalPriceRow(orderProduct: OrderProduct): number {
    let total: number;
    if (orderProduct.product.promotion) {
      total = orderProduct.product.price * orderProduct.orderQuantity;
      console.log(total + 'en promo');
    } else {
      total = orderProduct.product.promotionPrice * orderProduct.orderQuantity;
      console.log(total + 'pas promo');
    }
    return total;
  }

  getTotalPrice(): number {
    let total = 0;

    for (const key in this.caddy.items) {
      total += this.caddy.items[key].totalPriceRow;
    }
    return total;
  }

  getOrderProduct(): Array<OrderProduct> {
    const orderProducts: Array<OrderProduct> = new Array<OrderProduct>();

    for (const key in this.caddy.items) {
      orderProducts.push(this.caddy.items[key]);
    }
    return orderProducts;
  }

  getSize() {
    return Object.keys(this.caddy.items).length;
  }

}
