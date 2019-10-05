import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {FourHoFourComponent} from './four-ho-four/four-ho-four.component';
import {ErrorComponent} from './error/error.component';
import {CaddiesComponent} from './caddies/caddies.component';
import {ProductComponent} from './product/product.component';
import {AuthGuardService} from '../services/auth-gard.service';
import {ProductsComponent} from './products/products.component';
import {RegistrationComponent} from './registration/registration.component';
import {LoginComponent} from './login/login.component';
import {RouterModule, Routes} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {AuthenticationService} from '../services/authentification.service';
import {CaddyService} from '../services/caddy.service';
import {ProductService} from '../services/product.service';
import {CategoryService} from '../services/category.service';
import {APIService} from '../services/api.service';
import {TokenInterceptor} from '../interceptor/interceptor';
import {JwtModule, JwtModuleOptions} from '@auth0/angular-jwt';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { OrdersComponent } from './orders/orders.component';
import { OrderComponent } from './order/order.component';
import { RecoveryComponent } from './recovery/recovery.component';
import { AccountUpdateComponent } from './account-update/account-update.component';

const appRoutes: Routes = [
  {path: 'login', component: LoginComponent, children: [{path: ':redirect', component: LoginComponent}]},
  {path: 'recovery', component: RecoveryComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'account', component: AccountUpdateComponent , canActivate: [AuthGuardService]},
  {path: 'products', component: ProductsComponent},
  {path: 'product/:id', component: ProductComponent},
  {path: 'orders', canActivate: [AuthGuardService], component: OrdersComponent, children: [{path: ':admin', component: OrdersComponent}]},
  {path: 'order/:id', canActivate: [AuthGuardService], component: OrderComponent},
  {path: 'caddies', component: CaddiesComponent},
  {path: '', redirectTo: 'products', pathMatch: 'full'},
  {path: 'error', component: ErrorComponent},
  {path: 'not-found', component: FourHoFourComponent},
  {path: '**', redirectTo: 'not-found'}
];

const JWTModuleOptions: JwtModuleOptions = {
  config: {
    tokenGetter: tokenGetter,
    whitelistedDomains: ['localhost:4200']
  }
};

export function tokenGetter() {
  return localStorage.getItem('token');
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    FourHoFourComponent,
    ErrorComponent,
    ProductsComponent,
    ProductComponent,
    CaddiesComponent,
    OrdersComponent,
    OrderComponent,
    RecoveryComponent,
    AccountUpdateComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    HttpClientModule,
    JwtModule.forRoot(JWTModuleOptions),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader, // exported factory function needed for AoT compilation
        deps: [HttpClient]
      }
    })
  ],
  providers: [AuthenticationService, CaddyService, AuthGuardService, ProductService, CategoryService, APIService, {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
