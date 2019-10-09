import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentification.service';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {CategoryService} from '../../services/category.service';
import {Category} from '../../model/category.model';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit, OnDestroy {

  public error: string;
  public errors: Array<any>;
  // confirmation de mise à jour
  public success: string;
  public operation: string;
  public categoryId: string;
  public category: Category;
  public events;

  constructor(public authService: AuthenticationService,
              public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public categoryService: CategoryService,
              public titleService: Title) { }

  ngOnInit() {
    this.titleService.setTitle('Gestion des catégories');
    if (!this.authService.isAdmin()) {
      this.router.navigateByUrl('/404');
    }
    this.events = this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/category/')) {
        this.setContext();
      }
    });
    this.setContext();
  }

  ngOnDestroy(): void {
    this.events.unsubscribe();
  }

  setContext() {
    this.setNullErrorAndSuccess();
    this.category = null;
    this.categoryId = null;
    // récupération de l'opération à éffectué
    if (this.activeRoute.snapshot.paramMap.get('operation')) {
      this.operation = this.activeRoute.snapshot.paramMap.get('operation');
    } else {
      this.router.navigateByUrl('/404');
    }
    if (this.operation === 'create' || this.operation === 'edit') {
      this.category = new Category();
    } else {
      this.router.navigateByUrl('/404');
    }
  }

  setCurrentCategory(data) {
    this.categoryId = data.categoryId;
    this.api.getRessources<Category>('/p12-stock/public/category/' + this.categoryId).subscribe(cat => {
      this.category = cat;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  onSubmitCreateCategory(value: any) {
    this.setNullErrorAndSuccess();
    this.api.postRessources<Category>('/p12-stock/adminRole/category', value).subscribe(data => {
      this.success = 'Création réussie !';
      this.categoryService.setCategories();
      this.category.name = null;
    }, error1 => {
      if (error1.error.error) {
        this.error = error1.error.error;
      }
      if (error1.error.errors) {
        this.errors = error1.error.errors;
      }
    });
  }

  onSubmitEditCategory(value: any) {
    this.setNullErrorAndSuccess();
    this.api.putRessources<Category>('/p12-stock/adminRole/category/' + this.categoryId, value).subscribe(data => {
      this.success = 'Mise à jour réussie !';
      this.category = data;
      this.categoryService.setCategories();
    }, error1 => {
      if (error1.error.error) {
        this.error = error1.error.error;
      }
      if (error1.error.errors) {
        this.errors = error1.error.errors;
      }
    });
  }

  setNullErrorAndSuccess() {
    this.success = null;
    this.errors = null;
    this.error = null;
  }
}
