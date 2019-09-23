import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentification.service';
import {Product} from '../../model/product.model';
import {CaddyService} from '../../services/caddy.service';
import {PageProduct} from '../../model/page-product.model';
import {Category} from '../../model/category.model';

@Component({
  selector: 'app-product',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  public title;
  public size = 8;
  public page = 0;
  public products: PageProduct;
  public listSize: Array<number> = [4, 8, 12];
  public paramCategoryId: string;

  constructor(public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public authService: AuthenticationService,
              public caddyService: CaddyService) {
  }

  ngOnInit() {
    // on écoute un changement de route
    this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/products')) {
        this.setParamCategoryId();
        this.checkProductsHome(this.page, this.size);
      }
    });

    this.setParamCategoryId();
    // s'il y a rafraichissement manuel de la page
    this.checkProductsHome(this.page, this.size);

  }

  setParamCategoryId() {
    this.paramCategoryId = undefined;
    this.activeRoute.url.subscribe(() => {
      if (this.activeRoute.snapshot.firstChild) {
        if (this.activeRoute.snapshot.firstChild.paramMap.get('categoryId')) {
          this.paramCategoryId = this.activeRoute.snapshot.firstChild.paramMap.get('categoryId');
        }
      }
    });
  }

  private getProduct(url) {
    this.api.getRessources<PageProduct>(url).subscribe(data => {
      this.products = data;
    });
  }

  checkProductsHome(page, size) {
     if (this.paramCategoryId) {
         this.api.getRessources<Category>('/p12-stock/public/category/' + this.paramCategoryId).subscribe(cat => {
           if (cat) {
             this.title = 'Produit de la catégorie ' + cat.name;
             this.getProduct('/p12-stock/public/productsByCategoryId/' + this.paramCategoryId + '/' + page + '/' + size);
           }
         });
    } else {
       this.title = 'Produit en promotion';
       this.getProduct('/p12-stock/public/productsByPromotion/' + page + '/' + size);
     }

  }

  onEditProduct(p: Product) {
    // on encode en base64url pour un test
    // let url = btoa(p._links.product.href);
    this.router.navigateByUrl('/edit-product/' + p.id);
  }

  onAddProductToCaddy(p: Product) {
    this.caddyService.addProductToCaddy(p);
  }


  onSearchByPhiltre(data) {
    this.size = data.size;
    this.checkProductsHome(this.page, this.size);
  }
}
