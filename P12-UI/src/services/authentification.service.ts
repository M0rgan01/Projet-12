import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Router} from '@angular/router';

@Injectable()
export class AuthenticationService {

  public jwtTokenAuth: string = null;
  public jwtTokenRefresh: string = null;
  private roles: Array<any> = null;
  private sub: string = null;

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService, private router: Router) {
  }


  ///////////////////////// GESTION DES TOKENS ////////////////////

  removeToken() {
    this.jwtTokenAuth = null;
    this.jwtTokenRefresh = null;
    localStorage.removeItem('tokenAuth');
    localStorage.removeItem('tokenRefresh');
  }

  // sauvegarde du token dans le local storage
  saveToken(jwtAuth: string, jwtRefresh: string) {
    localStorage.setItem('tokenAuth', jwtAuth);
    localStorage.setItem('tokenRefresh', jwtRefresh);
  }

  // chargement du token d'auth
  loadTokenAuth() {
    this.jwtTokenAuth = localStorage.getItem('tokenAuth');
  }

  // chargement du token de refresh
  loadTokenRefresh() {
    this.jwtTokenRefresh = localStorage.getItem('tokenRefresh');
  }

  getTokenAuth() {
    if (this.jwtTokenAuth == null) {
      this.loadTokenAuth();
    }
    return this.jwtTokenAuth;
  }

  getTokenRefresh() {
    if (this.jwtTokenRefresh == null) {
      this.loadTokenRefresh();
    }
    return this.jwtTokenRefresh;
  }

  ///////////////////////// GESTION DES PAYLOAD ////////////////////

  // chargement du pseudo stoké dans le token
  getUserName() {
    if (this.jwtTokenAuth == null) {
      this.loadTokenAuth();
    }
    this.sub = this.jwtHelper.decodeToken(this.jwtTokenAuth).sub;
    return this.sub;
  }


  // vérification du role ADMIN
  isAdmin() {
    this.loadRoles();
    for (const r of this.roles) {
      if (r === 'ROLE_ADMIN') {
        return true;
      }
    }
    return false;
  }

  // chargement des roles stockés dans le token
  loadRoles() {
    if (this.jwtTokenAuth == null) {
      this.loadTokenAuth();
    }
    this.roles = this.jwtHelper.decodeToken(this.jwtTokenAuth).roles;
  }

  // verification de connection d'un utilisateur
  isAuth() {
    // this.chechTokenRefresh();
    this.loadTokenAuth();
    return this.jwtTokenAuth;
  }

  // méthode de déconection
  logout() {
    this.removeToken();
    this.router.navigateByUrl('/login');
  }

}

