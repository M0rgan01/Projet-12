import {Injectable} from '@angular/core';
import {APIService} from './api.service';
import {Router} from '@angular/router';
import {Category} from '../model/category.model';

@Injectable()
export class CategoryService {

  constructor(private api: APIService, private router: Router){
    this.getCategories();
  }

  categories: Array<Category>;

  getCategories() {
     this.api.getRessources<Array<Category>>('/p12-stock/categories').subscribe(data => {
       this.categories = data;
    }, error1 => {
       console.log(error1.message);
       this.router.navigateByUrl('/error)');
     });
  }


}
