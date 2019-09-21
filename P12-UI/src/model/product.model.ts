import {Category} from './category.model';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  promotionPrice: number;
  promotion: boolean;
  selected: boolean;
  available: boolean;
  quantity: number;
  capacity: number;
  photo: string;
  category: Category;
}
