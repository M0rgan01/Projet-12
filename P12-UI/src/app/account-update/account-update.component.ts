import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user.model';
import {APIService} from '../../services/api.service';
import {AuthenticationService} from '../../services/authentification.service';
import {Mail} from '../../model/mail.model';

@Component({
  selector: 'app-account-update',
  templateUrl: './account-update.component.html',
  styleUrls: ['./account-update.component.css']
})
export class AccountUpdateComponent implements OnInit {

  public error: string = null;
  public errors: Array<any> = null;
  public update: boolean;
  public user: User;

  constructor(public api: APIService, public authService: AuthenticationService) {
  }

  ngOnInit() {
    this.api.getRessources<User>('/p12-account/userRole/userByUserName/' + this.authService.getUserName()).subscribe(value => {
      this.user = value;
    }, error1 => {
      if (error1.status === 400) {
        if (error1.error.errors) {
          this.errors = error1.error.errors;
        }
      } else {
        this.error = 'Une erreur est survenue, veuillez réesayer plus tard';
      }
    });
  }


  onUpdateEmail(user: User) {
    this.error = null;
    this.errors = null;
    this.update = false;
    this.api.putRessources<Mail>('/p12-account/userRole/mail/' + user.id + '/' + user.mail.email, null).subscribe(value => {
      this.user.mail = value;
      this.update = true;
    }, error1 => {
      if (error1.status === 400) {
        if (error1.error.error) {
          this.error = error1.error.error;
        }
      } else {
        this.error = 'Une erreur est survenue, veuillez réesayer plus tard';
      }
    });
  }

  onUpdatePassWord(user: User) {
    this.error = null;
    this.errors = null;
    this.update = false;
    this.api.putRessources<null>('/p12-account/userRole/updatePassWord/' + user.id, user).subscribe(value => {
      this.update = true;
    }, error1 => {
      if (error1.status === 400) {
        if (error1.error.errors) {
          this.errors = error1.error.errors;
        } else if (error1.error.error) {
          this.error = error1.error.error;
        }
      } else {
        this.error = 'Une erreur est survenue, veuillez réesayer plus tard';
      }
    });
  }
}
