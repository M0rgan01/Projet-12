import {ItemProduct} from "./item-product.model";
import {Contact} from "./contact.model";

export class Caddy {

  items: Map<number, ItemProduct> = new Map();
  contact: Contact;

  constructor() {}
}
