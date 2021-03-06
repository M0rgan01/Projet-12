import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {Router} from '@angular/router';
import {Mail} from '../../model/mail.model';
import {User} from '../../model/user.model';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.css']
})
export class RecoveryComponent implements OnInit {
  public errors: Array<any> = null;
  public error: string;
  public email: string;
  public mail: Mail;
  public token: string;
  public user: User = new User();
  public loading: boolean;

  constructor(public api: APIService, public router: Router, public titleService: Title) {
  }

  ngOnInit() {
    this.titleService.setTitle('Récupération du mot de passe');
  }

  onSubmitEmail(email: string) {
    this.loading = true;
    this.api.putRessources<Mail>('/p12-account/public/sendTokenForRecovery/' + email, null).subscribe(value => {
      this.mail = value;
      this.error = null;
    }, error1 => {
      this.loading = false;
      this.error = error1.error.error;
    }, () => {
      this.loading = false;
    });
  }

  onSubmitToken(token: string) {
    this.loading = true;
    this.api.putRessources<Mail>('/p12-account/public/validateTokenForRecovery/' + this.email + '/' + token, null).subscribe(value => {
      this.mail = value;
      this.error = null;
    }, error1 => {
      this.loading = false;
      this.error = error1.error.error;
    }, () => {
      this.loading = false;
    });
  }

  onSubmitPassWord(user: User) {
    this.loading = true;
    this.api.putRessources<null>('/p12-account/public/editPassWordByRecovery/' + this.email, user).subscribe(value => {
      this.error = null;
      this.router.navigateByUrl('/login/returnRecovery');
    }, error1 => {
      this.loading = false;
      if (error1.error.error) {
        this.error = error1.error.error;
      }
      if (error1.error.errors) {
        this.errors = error1.error.errors;
      }
    }, () => {
      this.loading = false;
    });
  }
}
