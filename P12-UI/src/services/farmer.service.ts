import {Injectable} from '@angular/core';
import {APIService} from './api.service';
import {Router} from '@angular/router';
import {Farmer} from '../model/farmer.model';

@Injectable()
export class FarmerService {

  public farmers: Array<Farmer>;

  constructor(private api: APIService, private router: Router) {
    this.setFarmers();
  }

  setFarmers() {
    this.api.getRessources<Array<Farmer>>('/p12-stock/public/farmers').subscribe(data => {
      this.farmers = data;
    }, error1 => {
      this.router.navigateByUrl('/error)');
    });
  }
}
