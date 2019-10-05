import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../services/authentification.service';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {SearchCriteria} from '../../model/search-criteria.model';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  public object: string;

  constructor(public authService: AuthenticationService,
              public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute) { }

  ngOnInit() {
    if (!this.authService.isAdmin()) {
      this.router.navigateByUrl('/404');
    }
    this.getCurrentObjectToCreate();
    this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/create')) {
        this.getCurrentObjectToCreate();
      }
    });
  }

  getCurrentObjectToCreate() {
    const object = this.activeRoute.snapshot.paramMap.get('object');
    if (object === 'category' || object === 'product' || object === 'farmer') {
      this.object = object;
    } else {
      this.router.navigateByUrl('/404');
    }
    console.log(object);
  }
}
