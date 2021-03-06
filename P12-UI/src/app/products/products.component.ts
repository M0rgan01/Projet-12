import {Component, OnDestroy, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {CaddyService} from '../../services/caddy.service';
import {Category} from '../../model/category.model';
import {Page} from '../../model/page.model';
import {Product} from '../../model/product.model';
import {SearchCriteria} from '../../model/search-criteria.model';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-product',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {

  public title;
  public size = 8;
  public page = 0;
  public products: Page<Product>;
  public paramCategoryId: string;
  public paramSearch: string;
  public listSearchCriteria: Array<SearchCriteria>;
  public searchCriteria: SearchCriteria;
  public listSize: Array<number> = [4, 8, 12];
  public listFilter: Array<string> = ['Nom', 'Prix', 'Promotion', 'Aucun'];
  public filter: string;
  public events;

  constructor(public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public caddyService: CaddyService,
              public titleService: Title) {
  }

  ngOnInit() {

    this.listSearchCriteria = new Array<SearchCriteria>();
    // on écoute un changement de route
    this.events = this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/products')) {
        this.listSearchCriteria = new Array<SearchCriteria>();
        this.setParam();
        this.checkProductsHome(this.page, this.size);
      }
    });

    this.setParam();
    // s'il y a rafraichissement manuel de la page
    this.checkProductsHome(this.page, this.size);

  }

  ngOnDestroy(): void {
    this.events.unsubscribe();
  }

  setParam() {
    this.activeRoute.queryParamMap.subscribe(params => {
      this.paramCategoryId = params.get('category');
      this.paramSearch = params.get('search');
    });
  }

  private getProduct(page, size, object) {
    this.api.postRessources<Page<Product>>('/p12-stock/public/searchProduct/' + page + '/' + size, object).subscribe(data => {
      this.products = data;
    });
  }

  checkProductsHome(page, size) {
    window.scroll(0, 0);
    if (this.paramCategoryId) {
      this.api.getRessources<Category>('/p12-stock/public/category/' + this.paramCategoryId).subscribe(cat => {
        if (cat) {
          this.title = 'Produit de la catégorie ' + cat.name;
          this.titleService.setTitle('Produit de la catégorie ' + cat.name);
          this.searchCriteria = new SearchCriteria('category.id', ':', cat.id);
          this.listSearchCriteria.push(this.searchCriteria);
          this.getProduct(page, size, this.listSearchCriteria);
        }
      }, error1 => {
      });
    } else if (this.paramSearch) {
      this.title = 'Recherche : ' + this.paramSearch;
      this.titleService.setTitle('Recherche : ' + this.paramSearch);
      this.searchCriteria = new SearchCriteria('name', ':', this.paramSearch);
      this.listSearchCriteria.push(this.searchCriteria);
      this.getProduct(page, size, this.listSearchCriteria);
    } else {
      this.title = 'Produit en promotion';
      this.titleService.setTitle('Produit en promotion');
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
