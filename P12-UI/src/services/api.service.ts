import {Injectable,} from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from '@angular/common/http';
import {Observable, throwError, timer} from 'rxjs';
import {mergeMap, retryWhen} from 'rxjs/operators';
import {Router} from '@angular/router';


@Injectable()
export class APIService {

  host = 'http://localhost:9103/api';

  constructor(private http: HttpClient, private router: Router) {
  }


  /////////////////////////////// AUTHENTICATION /////////////////////////////////

  // méthode de connection d'un utilisateur
  login(user) {
    return this.http.post(this.host + '/auth/login', user, {observe: 'response'});
  }

  sendRefreshToken() {
    return this.http.get(this.host + '/auth/token', {observe: 'response'});
  }

  // inscritpion
  register(contact) {
    return this.http.post(this.host + '/auth/register', contact, {observe: 'response'});
  }

  /////////////////////////////// RESSOURCES /////////////////////////////////

  getRessources<T>(url): Observable<T> {
    return this.http.get<T>(this.host + url).pipe(
      retryWhen(this.genericRetryStrategy()));
  }

  putRessources<T>(url, obj): Observable<T> {
    return this.http.put<T>(this.host + url, obj).pipe(
      retryWhen(this.genericRetryStrategy()));
  }


  postRessources<T>(url, obj): Observable<T> {
    return this.http.post<T>(this.host + url, obj).pipe(
      retryWhen(this.genericRetryStrategy()));
  }


  /////////////////////////////// UPLOAD /////////////////////////////////

  uploadPhoto(file: File, url) {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post(this.host + url, formData, {reportProgress: true, observe: 'events'}).pipe(
      retryWhen(this.genericRetryStrategy()));
  }

  /////////////////////////////// ERROR /////////////////////////////////

  redirectToError() {
    this.router.navigateByUrl('/error');
  }

  /////////////////////////////// NotFound /////////////////////////////////

  redirectToNotFound() {
    this.router.navigateByUrl('/404');
  }

  /////////////////////////////// RETRY /////////////////////////////////

  genericRetryStrategy = ({
                            maxRetryAttempts = 1,
                            scalingDuration = 200,
                            onlyStatusCodes = [401]
                          }: {
    maxRetryAttempts?: number,
    scalingDuration?: number,
    onlyStatusCodes?: number[]
  } = {}) => (attempts: Observable<any>) => {
    return attempts.pipe(
      mergeMap((error, i) => {
        const retryAttempt = i + 1;
        // if maximum number of retries have been met
        // or response is a status code we don't wish to retry, throw error
        if (
          retryAttempt > maxRetryAttempts ||
          !onlyStatusCodes.find(e => e === error.status)
        ) {
          return throwError(error);
        }
        console.log(
          `Attempt ${retryAttempt}: retrying in ${retryAttempt *
          scalingDuration}ms`
        );
        // retry after 1s, 2s, etc...
        return timer(retryAttempt * scalingDuration);
      }));
  }
}
