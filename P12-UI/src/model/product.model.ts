import {Category} from './category.model';

export class Product {
  id: number;
  name: string;
  description: string;
  price: number;
  oldPrice: number;
  promotion: boolean;
  available: boolean;
  quantity: number;
  capacity: number;
  photo: string;
  category: Category;
  orderQuantity: number;
  measure: string;
}
