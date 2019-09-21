import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';
import {catchError} from 'rxjs/operators';
import {BehaviorSubject, throwError} from 'rxjs';
import {Injectable} from '@angular/core';
import {AuthenticationService} from '../services/authentification.service';
import {APIService} from '../services/api.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  isRefreshingToken = false;
  tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  constructor(private authService: AuthenticationService, private api: APIService) {
  }


  addToken(req: HttpRequest<any>): HttpRequest<any> {

    const refreshToken = this.authService.getTokenRefresh();
    const authToken = this.authService.getTokenAuth();

    // s'il y a un token, on l'ajoute au header de la requete
    if (authToken && refreshToken) {
      return req = req.clone({
        setHeaders: {
          Authorization: authToken,
          refresh: refreshToken
        }
      });
    }

    return req;
  }


  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(this.addToken(request)).pipe(catchError(err => {

      if (err.status === 401) {
        if (err.error.message === 'jwt.expired') {

          this.api.sendRefreshToken().subscribe(resp => {
            // nous définisson un object jwt qui aura pour valeur l'en-tete
            if (resp.status === 200) {
              const jwtAuth = resp.headers.get('Authorization');
              const jwtRefresh = resp.headers.get('refresh');
              // on enregistrons le jwt dans le localStorage d'angular
              this.authService.saveToken(jwtAuth, jwtRefresh);
              this.tokenSubject.next(jwtAuth);
            }
          }, error => {
            this.authService.logout();
          }, () => {
            this.isRefreshingToken = false;
          });

        }
      } else if (err.status === 406) {
        if (err.error.message === 'jwt.expired' || err.error.message === 'jwt.invalid') {
          this.authService.logout();
        }
      } else if (err.status === 500) {
        this.api.redirectToError();
      }

      return throwError(err);
    }));
  }


}






































