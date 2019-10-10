import {Injectable} from '@angular/core';
import {Caddy} from '../model/caddy.model';
import {Product} from '../model/product.model';
import {OrderProduct} from '../model/order-product.model';


@Injectable()
export class CaddyService {

  public caddy;

  constructor() {
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

  removeCaddy() {
    localStorage.removeItem('myCaddies');
  }

  addProductToCaddy(product: Product) {
    if (product.orderQuantity <= 0 || product.orderQuantity >= 100) {
      alert('Quantitée incorrect : de 1 à 99');
    } else {
      let orderProduct = this.caddy.items[product.id];
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
  }

  removeProductToCaddy(product: Product) {
    if (this.caddy.items[product.id]) {
      delete this.caddy.items[product.id];
      this.saveCaddies();
    }
    if (this.getSize() === 0) {
      this.removeCaddy();
    }
  }

  getTotalPriceRow(orderProduct: OrderProduct): number {
    let total: number;
    total = orderProduct.product.price * orderProduct.orderQuantity;
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
