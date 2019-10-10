import {CategoryService} from '../services/category.service';
import {Router} from '@angular/router';
import {CaddyService} from '../services/caddy.service';
import {APIService} from '../services/api.service';
import {AuthenticationService} from '../services/authentification.service';
import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  public title = 'Panier garni';
  public isTogled = false;

  constructor(public authService: AuthenticationService,
              public api: APIService,
              public caddyService: CaddyService,
              public router: Router,
              public categoryService: CategoryService,
              public translate: TranslateService,
              public titleService: Title) {
    translate.setDefaultLang('fr');
    translate.use('fr');
  }


// route de deconnection
  onLogout() {
    this.authService.logout();
  }

  // déroulement de la sidebar
  onSwitchToggle() {
    if (this.isTogled) {
      this.isTogled = false;
    } else if (!this.isTogled) {
      this.isTogled = true;
    }
  }

  onSearch(data) {
    this.router.navigate(['/products'], { queryParams: { search: data.name } });
  }

  ngOnInit(): void {
    this.titleService.setTitle('Panier garni');
  }

}
