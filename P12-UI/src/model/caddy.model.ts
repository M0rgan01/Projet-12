import {OrderProduct} from './order-product.model';

export class Caddy {
  items: Map<number, OrderProduct> = new Map();
}
