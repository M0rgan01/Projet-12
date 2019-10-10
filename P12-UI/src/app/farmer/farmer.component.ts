import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentification.service';
import {APIService} from '../../services/api.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {FarmerService} from '../../services/farmer.service';
import {Farmer} from '../../model/farmer.model';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-farmer',
  templateUrl: './farmer.component.html',
  styleUrls: ['./farmer.component.css']
})
export class FarmerComponent implements OnInit, OnDestroy {

  public error: string;
  public errors: Array<any>;
  // confirmation de mise à jour
  public success: string;
  public operation: string;
  public farmerId: string;
  public farmer: Farmer;
  public events;


  constructor(public authService: AuthenticationService,
              public api: APIService,
              public router: Router,
              public activeRoute: ActivatedRoute,
              public farmerService: FarmerService,
              public titleService: Title) {
  }

  ngOnInit() {
    this.titleService.setTitle('Gestion des agriculteurs');
    if (!this.authService.isAdmin()) {
      this.router.navigateByUrl('/404');
    }
    this.events = this.router.events.subscribe((val) => {
      // si la navigation arrive à terme (il y a plusieur event, donc on en garde que un pour éviter plusieur éxécution)
      if (val instanceof NavigationEnd && val.url.startsWith('/farmer/')) {
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
    this.farmer = null;
    this.farmerId = null;
    // récupération de l'opération à éffectué
    if (this.activeRoute.snapshot.paramMap.get('operation')) {
      this.operation = this.activeRoute.snapshot.paramMap.get('operation');
    } else {
      this.router.navigateByUrl('/404');
    }
    if (this.operation === 'create' || this.operation === 'edit') {
      this.farmer = new Farmer();
    } else {
      this.router.navigateByUrl('/404');
    }
  }

  setCurrentFarmer(data) {
    this.farmerId = data.farmerId;
    this.api.getRessources<Farmer>('/p12-stock/public/farmer/' + this.farmerId).subscribe(far => {
      this.farmer = far;
    }, error1 => {
      this.router.navigateByUrl('/error');
    });
  }

  onSubmitCreateCategory(value: any) {
    this.setNullErrorAndSuccess();
    this.api.postRessources<Farmer>('/p12-stock/adminRole/farmer', value).subscribe(data => {
      this.success = 'Création réussie !';
      this.farmer.name = null;
      this.farmer.location = null;
      this.farmer.phone = null;
      this.farmerService.setFarmers();
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
    this.api.putRessources<Farmer>('/p12-stock/adminRole/farmer/' + this.farmerId, value).subscribe(data => {
      this.success = 'Mise à jour réussie !';
      this.farmerService.setFarmers();
      this.farmer = data;
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
