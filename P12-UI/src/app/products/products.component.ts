import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {CaddyService} from '../../services/caddy.service';
import {Category} from '../../model/category.model';
import {Page} from '../../model/page.model';
import {Product} from '../../model/product.model';
import {SearchCriteria} from '../../model/search-criteria.model';

@Component({
  selector: 'app-product',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  public title;
  public size = 8;
  public page = 0;
  public products: Page<Product>;
  public listSize: Array<number> = [4, 8, 12];
  public paramCategoryId: string;
  public listSearchCriteria: Array<SearchCriteria>;
  public searchCriteria: SearchCriteria;
  public listFilter: Array<string> = ['Nom', 'Prix', 'Promotion', 'Aucun'];
  public filter: string;

  constructor(public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public caddyService: CaddyService) {
  }

  ngOnInit() {
    this.listSearchCriteria = new Array<SearchCriteria>();
    // on écoute un changement de route
    this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/products')) {
        this.listSearchCriteria = new Array<SearchCriteria>();
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

  private getProduct(page, size, object) {
    this.api.postRessources<Page<Product>>('/p12-stock/public/searchProduct/' + page + '/' + size, object).subscribe(data => {
      this.products = data;
    });
  }

  checkProductsHome(page, size) {
    if (this.paramCategoryId) {
      this.api.getRessources<Category>('/p12-stock/public/category/' + this.paramCategoryId).subscribe(cat => {
        if (cat) {
          this.title = 'Produit de la catégorie ' + cat.name;
          this.searchCriteria = new SearchCriteria('category.id', ':', cat.id);
          this.listSearchCriteria.push(this.searchCriteria);
          this.getProduct(page, size, this.listSearchCriteria);
        }
      }, error1 => {
      });
    } else {
      this.title = 'Produit en promotion';
      this.searchCriteria = new SearchCriteria('promotion', ':', true);
      this.listSearchCriteria.push(this.searchCriteria);
      this.getProduct(page, size, this.listSearchCriteria);
    }

  }

  onSearchByPhiltre(data) {
    this.listSearchCriteria = new Array<SearchCriteria>();
    this.filter = data.filter;
    if (data.searchByName !== '') {
      this.searchCriteria = new SearchCriteria('name', ':', data.searchByName);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchMinPrice !== null && data.searchMaxPrice !== '') {
      this.searchCriteria = new SearchCriteria('price', '>', data.searchMinPrice);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchMaxPrice !== null && data.searchMaxPrice !== '') {
      this.searchCriteria = new SearchCriteria('price', '<', data.searchMaxPrice);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchAvailable) {
        this.searchCriteria = new SearchCriteria('available', ':', true);
        this.listSearchCriteria.push(this.searchCriteria);
    }

    if (this.filter !== null && this.filter !== 'Aucun') {
      if (this.filter === 'Nom') {
        this.searchCriteria = new SearchCriteria('name', 'ORDER_BY_ASC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      } else if (this.filter === 'Prix') {
        this.searchCriteria = new SearchCriteria('price', 'ORDER_BY_ASC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      } else if (this.filter === 'Promotion') {
        this.searchCriteria = new SearchCriteria('promotion', 'ORDER_BY_DESC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      }
    }
    this.size = data.size;
    this.checkProductsHome(this.page, this.size);
  }
}
