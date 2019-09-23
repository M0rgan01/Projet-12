import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from 'src/services/authentification.service';
import {ActivatedRoute, Router} from '@angular/router';
import {APIService} from '../../services/api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public error: string = null;
  public paramRedirect: string;

  constructor(public authService: AuthenticationService,
              public api: APIService, public router: Router,
              public activeRoute: ActivatedRoute) {
  }

  ngOnInit() {

    this.activeRoute.url.subscribe(() => {
        if (this.activeRoute.snapshot.firstChild) {
          if (this.activeRoute.snapshot.firstChild.paramMap.get('redirect')) {
            this.paramRedirect = this.activeRoute.snapshot.firstChild.paramMap.get('redirect');
          }
        }
      }
    );
  }

  onLogin(dataForm) {
    // si on est déja connecté, alors on logout
    if (this.authService.isAuth()) {
      this.authService.logout();
    }

    this.api.login(dataForm).subscribe(resp => {
      // nous définisson un object jwt qui aura pour valeur l'en-tete Authorization
      const jwtAuth = resp.headers.get('Authorization');
      const jwtRefresh = resp.headers.get('refresh');
      // on enregistrons le jwt dans le localStorage d'angular
      this.authService.saveToken(jwtAuth, jwtRefresh);
      if (this.paramRedirect) {
        if (this.paramRedirect === 'returnCaddy') {
          this.router.navigateByUrl('/caddies');
        } else {
          this.router.navigateByUrl('/404');
        }
      } else {
        this.router.navigateByUrl('/');
      }
    }, err => {
      this.error = err.error.error;
    });
  }

}
