import {Injectable} from '@angular/core';
import {APIService} from './api.service';
import {Router} from '@angular/router';

@Injectable()
export class ProductService {



  constructor(private api : APIService, private router: Router){}




}
