import {Product} from './product.model';

export class PageProduct {
  content: Array<Product>;
  totalPages: number;
  totalElements: number;
  number: number;
  numberOfElements: number;
}
