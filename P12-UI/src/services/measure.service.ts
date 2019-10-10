import {Injectable} from '@angular/core';
import {APIService} from './api.service';
import {Router} from '@angular/router';

@Injectable()
export class MeasureService {

  public measures: Array<string>;

  constructor(private api: APIService, private router: Router) {
    this.getMeasures();
  }

  getMeasures() {
    this.api.getRessources<Array<string>>('/p12-stock/public/product/measures').subscribe(data => {
      this.measures = data;
    }, error1 => {
      this.router.navigateByUrl('/error)');
    });
  }

}
