import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentification.service';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {Product} from '../../model/product.model';
import {CaddyService} from '../../services/caddy.service';
import {CategoryService} from '../../services/category.service';
import {FarmerService} from '../../services/farmer.service';
import {MeasureService} from '../../services/measure.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-edit-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit, OnDestroy {


  // erreur à afficher dans le cas d'un produit non conforme
  public errorUpload: string;
  public successUpload: string;
  public error: string;
  public errors: Array<any>;
  // confirmation de mise à jour
  public success: string;
  // produit récupérer par rapport à son id
  public product: Product;
  // fichier(s) séléctionné(s) par l'utilisateur
  public selectedFile;
  public currentFileUpload;
  // progression de l'upload en serveur
  public progress = 0;
  // https://www.youtube.com/watch?v=sWX-PAyxphU&t=1197s
  public timeStamp = 0;
  public operation: string;
  public productId: string;
  public events;

  constructor(public authService: AuthenticationService,
              public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public caddyService: CaddyService,
              public categoryService: CategoryService,
              public farmerService: FarmerService,
              public measureService: MeasureService,
              public titleService: Title) {
  }

  ngOnInit() {
    this.events = this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/product/')) {
        this.setContext();
      }
    });
    this.setContext();
  }

  ngOnDestroy(): void {
    this.events.unsubscribe();
  }

  setContext() {
    this.product = null;
    this.setNullErrorAndSuccess();
    // récupération de l'opération à éffectué
    if (this.activeRoute.snapshot.paramMap.get('operation')) {
      this.operation = this.activeRoute.snapshot.paramMap.get('operation');
    } else {
      this.router.navigateByUrl('/404');
    }

    // vérification de permission
    if (this.operation === 'edit' || this.operation === 'create') {
      if (!this.authService.isAdmin()) {
        this.router.navigateByUrl('/404');
      }
    }

    const a = this.activeRoute.url.subscribe(() => {
      if (this.operation === 'edit' || this.operation === 'details') {
        if (this.activeRoute.snapshot.firstChild && this.activeRoute.snapshot.firstChild.paramMap.get('id')) {
          this.productId = this.activeRoute.snapshot.firstChild.paramMap.get('id');
          this.getProduct(this.productId);
        } else {
          this.router.navigateByUrl('/404');
        }
      } else if (this.operation === 'create') {
        this.titleService.setTitle("Création d'un produit");
        this.product = new Product();
      } else {
        this.router.navigateByUrl('/404');
      }
    });
    a.unsubscribe();
  }

  getProduct(id) {
    // on récupère le produit
    this.api.getRessources<Product>('/p12-stock/public/product/' + id).subscribe(dataProduct => {
      this.product = dataProduct;
      this.titleService.setTitle('Produit : ' + this.product.name);
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

// formulaire de modification
  onSubmitEditProduct(data: Product) {
    window.scroll(0, 0);
    data.id = this.product.id;
    this.setNullErrorAndSuccess();
    this.api.putRessources<Product>('/p12-stock/adminRole/product/' + data.id, data).subscribe(dataProduct => {
      this.success = 'Mise à jour réussi !';
      this.product = dataProduct;
      this.titleService.setTitle('Produit : ' + this.product.name);
    }, error1 => {
      if (error1.error.error) {
        this.error = error1.error.error;
      }
      if (error1.error.errors) {
        this.errors = error1.error.errors;
      }
    });
  }

  // formulaire de création
  onSubmitCreateProduct(data: Product) {
    window.scroll(0, 0);
    this.setNullErrorAndSuccess();
    this.api.postRessources<Product>('/p12-stock/adminRole/product', data).subscribe(dataProduct => {
     this.router.navigateByUrl('/product/details/' + dataProduct.id);
    }, error1 => {
      if (error1.error.error) {
        this.error = error1.error.error;
      }
      if (error1.error.errors) {
        this.errors = error1.error.errors;
      }
    });
  }

// récupération de la photo
  onSelectedFile(event) {
    // selectedFile devient une liste de fichier
    this.selectedFile = event.target.files;
    // on récupère le seul fichier
    this.currentFileUpload = this.selectedFile.item(0);
  }


  uploadPhoto() {
    if (this.selectedFile !== undefined) {
      this.setNullErrorAndSuccess();
      this.progress = 0;
      this.api.uploadPhoto(this.currentFileUpload, '/p12-stock/adminRole/product/' + this.product.id + '/photo').subscribe(event => {
        // si le type est un événement de UploadProgress
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
          // si c'est terminé
        } else if (event instanceof HttpResponse) {
          this.successUpload = 'Mise à jour réussi !';
          this.timeStamp = Date.now();
          this.product.photo = ' ';
        }
      }, error1 => {
        this.errorUpload = 'Une erreur est survenue';
      });
    } else {
      this.errorUpload = 'Fichier non défini';
    }
  }


  setNullErrorAndSuccess() {
    this.success = null;
    this.errors = null;
    this.error = null;
    this.errorUpload = null;
    this.successUpload = null;
  }
}
