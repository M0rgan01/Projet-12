import {Product} from './product.model';

export class OrderProduct {
  id: number;
  productId: Product;
  orderQuantity: number;
  realQuantity: number;
  totalPriceRow: number;
  product: Product;
}
