import {ItemProduct} from './item-product.model';
import {User} from './user.model';

export class Caddy {

  items: Map<number, ItemProduct> = new Map();
  user: User;

  constructor() {}
}
