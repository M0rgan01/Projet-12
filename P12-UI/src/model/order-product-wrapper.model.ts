import {OrderProduct} from './order-product.model';

export class OrderProductWrapper {

  constructor(orderProducts: Array<OrderProduct>) {
    this.orderProducts = orderProducts;
  }

  public orderProducts: Array<OrderProduct>;
}
