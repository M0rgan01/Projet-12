import {Component, OnInit} from '@angular/core';
import {APIService} from '../../services/api.service';
import {AuthenticationService} from '../../services/authentification.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {Page} from '../../model/page.model';
import {Order} from '../../model/order.model';
import {SearchCriteria} from '../../model/search-criteria.model';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  constructor(public api: APIService,
              public authService: AuthenticationService,
              public router: Router,
              public activeRoute: ActivatedRoute) {
  }

  public adminOrder: string;
  public title: string;
  public size = 8;
  public page = 0;
  public orders: Page<Order>;
  public listSize: Array<number> = [8, 16, 32];
  public listFilter: Array<string> = ['Référence', 'Prix total', 'Date', 'Réception'];
  public filter: string;
  public listSearchCriteria: Array<SearchCriteria>;
  public searchCriteria: SearchCriteria;

  ngOnInit() {
    this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/orders')) {
        this.adminOrder = undefined;
        this.listSearchCriteria = undefined;
        this.setContext();
      }
    });
    this.setContext();
  }

  setContext() {
    this.activeRoute.url.subscribe(() => {
        if (this.activeRoute.snapshot.firstChild) {
          if (this.activeRoute.snapshot.firstChild.paramMap.get('admin')) {
            this.adminOrder = this.activeRoute.snapshot.firstChild.paramMap.get('admin');
          }
        }
      }
    );

    if (this.adminOrder) {
      if (this.authService.isAuth() && this.adminOrder === 'admin') {
        this.title = 'Administration des commandes';
        this.getOrders(this.page, this.size);
      } else {
        this.router.navigateByUrl('/404');
      }
    } else {
      this.title = 'Mes commandes';
      this.getOrdersByUserName(this.page, this.size, this.authService.getUserName());
    }
  }


  getOrdersByUserName(page, size, userName) {
    this.api.postRessources<Page<Order>>('/p12-order/userRole/orders/' +
      userName + '/' + page + '/' + size, this.listSearchCriteria).subscribe(value => {
      this.orders = value;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  getOrders(page, size) {
    this.api.postRessources<Page<Order>>('/p12-order/adminRole/orders/' + page + '/' + size, this.listSearchCriteria).subscribe(value => {
      this.orders = value;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  onSearchByPhiltre(data) {
    this.listSearchCriteria = new Array<SearchCriteria>();
    this.filter = data.filter;
    if (data.searchByRef !== '') {
      this.searchCriteria = new SearchCriteria('reference', ':', data.searchByRef);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchMinPrice !== null && data.searchMinPrice !== '') {
      this.searchCriteria = new SearchCriteria('totalPrice', '>', data.searchMinPrice);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchMaxPrice !== null && data.searchMaxPrice !== '') {
      this.searchCriteria = new SearchCriteria('totalPrice', '<', data.searchMaxPrice);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (data.searchNoCancel) {
      this.searchCriteria = new SearchCriteria('cancel', ':', false);
      this.listSearchCriteria.push(this.searchCriteria);
    }

    if (this.filter !== null && this.filter !== 'Aucun') {
      if (this.filter === 'Référence') {
        this.searchCriteria = new SearchCriteria('reference', 'ORDER_BY_ASC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      } else if (this.filter === 'Prix total') {
        this.searchCriteria = new SearchCriteria('totalPrice', 'ORDER_BY_ASC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      } else if (this.filter === 'Date') {
        this.searchCriteria = new SearchCriteria('date', 'ORDER_BY_DESC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      } else if (this.filter === 'Réception') {
        this.searchCriteria = new SearchCriteria('reception', 'ORDER_BY_DESC', null);
        this.listSearchCriteria.push(this.searchCriteria);
      }
    }
    this.size = data.size;
    if (data.searchByUserName !== '') {
      this.getOrdersByUserName(this.page, this.size, data.searchByUserName);
    } else {
      this.getOrders(this.page, this.size);
    }
  }
}
