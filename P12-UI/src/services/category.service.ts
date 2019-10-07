import {Injectable} from '@angular/core';
import {APIService} from './api.service';
import {Router} from '@angular/router';
import {Category} from '../model/category.model';

@Injectable()
export class CategoryService {

  constructor(private api: APIService, private router: Router) {
    this.setCategories();
  }

  public categories: Array<Category>;

  setCategories() {
     this.api.getRessources<Array<Category>>('/p12-stock/public/categories').subscribe(data => {
       this.categories = data;
    }, error1 => {
       this.router.navigateByUrl('/error)');
     });
  }

}
