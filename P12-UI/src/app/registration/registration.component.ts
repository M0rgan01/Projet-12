import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from 'src/services/authentification.service';
import {Router} from '@angular/router';
import {APIService} from 'src/services/api.service';
import {User} from '../../model/user.model';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  public error: string = null;
  public errors: Array<any> = null;
  public user: User = new User();

  constructor(public authService: AuthenticationService, public router: Router, public api: APIService, public titleService: Title) {
  }

  ngOnInit() {
    this.titleService.setTitle('Inscription');
  }

  onRegister(contactDTO: User) {
    this.error = null;
    this.errors = null;
    // si on est déja connecté, alors on reset les token
    if (this.authService.isAuth()) {
      this.authService.removeToken();
    }

    this.api.register(contactDTO).subscribe(resp => {
      // nous définisson un object jwt qui aura pour valeur l'en-tete Authorization
      const jwtAuth = resp.headers.get('Authorization');
      const jwtRefresh = resp.headers.get('refresh');
      // on enregistrons le jwt dans le localStorage d'angular
      this.authService.saveToken(jwtAuth, jwtRefresh);
      // on redirige vers l'url tasks
      this.router.navigateByUrl('/');
    }, err => {
      if (err.error.error) {
        this.error = err.error.error;
      }
      if (err.error.errors) {
        this.errors = err.error.errors;
      }
    });

  }

}
