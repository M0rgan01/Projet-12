import {Category} from './category.model';
import {Farmer} from './farmer.model';

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
  farmer: Farmer;
  measure: string;
  file: File;
}
