import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentification.service';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {Product} from '../../model/product.model';
import {CaddyService} from '../../services/caddy.service';

@Component({
  selector: 'app-edit-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  // object formulaire contenant un produit
  public productForm: FormGroup;
  // erreur à afficher dans le cas d'un produit non conforme
  public errorUpdate;
  public errorUpload;
  // booleans de confirmation de mise à jour
  public successUpdate: boolean;
  public successUpload: boolean;
  // produit récupérer par rapport à son id
  public product: Product;
  // fichier(s) séléctionné(s) par l'utilisateur
  public selectedFile;
  public currentFileUpload;
  // progression de l'upload en serveur
  public progress = 0;
  // https://www.youtube.com/watch?v=sWX-PAyxphU&t=1197s
  public timeStamp = 0;
  public modification: boolean;

  constructor(public authService: AuthenticationService,
              public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public formBuilder: FormBuilder,
              public caddyService: CaddyService) {
  }

  ngOnInit() {
    const paramProductId = this.activeRoute.snapshot.paramMap.get('id');
    // on récupère le produit
    this.api.getRessources<Product>('/p12-stock/public/product/' + paramProductId).subscribe(dataProduct => {
      this.product = dataProduct;
      this.productForm = this.formBuilder.group(dataProduct);
    }, error1 => {
      this.router.navigateByUrl('/error');
    });

  }

// formulaire de modification
  onSubmitProduct() {
    this.successUpdate = false;
    this.api.putRessources('/edit/products', this.productForm.value).subscribe(data => {
      this.productForm = this.formBuilder.group(data);
      this.successUpdate = true;
    }, error1 => {
      this.errorUpdate = error1.message;
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

      this.errorUpload = null;
      this.successUpload = false;
      this.progress = 0;

      this.api.uploadPhoto(this.currentFileUpload, '/p12-stock/adminRole/product/photo/' + this.product.id).subscribe(event => {
        // si le type est un événement de UploadProgress
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
          // si c'est terminé
        } else if (event instanceof HttpResponse) {
          this.successUpload = true;
          this.timeStamp = Date.now();
        }
      }, error1 => {
        this.errorUpload = 'Une erreur est survenue';
        console.log(error1.message);
      });
    } else {
      this.errorUpload = 'Fichier non défini';
    }
  }
}
