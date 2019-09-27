import {OrderProduct} from './order-product.model';

export class Order{
  public id: number;
  public reference: string;
  public totalPrice: number;
  public date: Date;
  public reception: Date;
  public paid: boolean;
  public cancel: boolean;
  public userId: number;
  public orderProducts: Array<OrderProduct>;
}
