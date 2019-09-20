import {Category} from "./category.model";

export interface Product {
  id: number;
  name: string;
  description: string;
  currentPrice: number;
  promotion: boolean;
  selected: boolean;
  available: boolean;
  quantite: number;
  stock: number;
  photo: string;
  category: Category;


  _links:{
    self:{
      href:string;
    },
    product:{
      href:string;
    }, category:{
      href:string;
    }
  }
}
