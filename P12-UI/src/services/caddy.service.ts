import {Injectable} from '@angular/core';
import {Caddy} from '../model/caddy.model';
import {Product} from '../model/product.model';
import {ItemProduct} from '../model/item-product.model';

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

  addProductToCaddy(product: Product) {
    let productItem = this.caddy.items[product.id];
    if (productItem === undefined) {
      productItem = new ItemProduct();
      productItem.id = product.id;
      productItem.price = product.price;
      productItem.quantite = product.quantity;
      productItem.product = product;
      this.caddy.items[product.id] = productItem;
    } else {
      productItem.quantite += product.quantity;
      productItem.price = productItem.product.currentPrice * productItem.quantite;
      this.caddy.items[product.id] = productItem;
    }
    this.saveCaddies();
  }

  removeProductToCaddy(product: Product){
      if(this.caddy.items[product.id]){
        delete this.caddy.items[product.id];
        this.saveCaddies();
      }
  }

  getTotalPrice(): number {
    let total = 0;

    for (const key in this.caddy.items) {
      total += this.caddy.items[key].product.currentPrice * this.caddy.items[key].quantite;
    }
    return total;
  }

  getSize() {
    return Object.keys(this.caddy.items).length;
  }

}
