import { Injectable, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRouteSnapshot, Resolve, Router, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { SimpleInventorySharedModule } from 'app/shared/shared.module';
import { InventoryComponent } from './inventory.component';
import { InventoryProductDetailComponent } from './inventory-product-detail.component';
import { IProduct, Product } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { EMPTY, Observable, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { flatMap } from 'rxjs/operators';
import { InventoryInternalTransferComponent } from './inventory-internal-transfer.component';
import { InventoryExternalTransferComponent } from './inventory-external-transfer.component';
import { IStock, Stock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock/stock.service';

@Injectable({ providedIn: 'root' })
export class InventoryResolve implements Resolve<IProduct> {
  constructor(private service: ProductService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findWithStock(id).pipe(
        flatMap((product: HttpResponse<Product>) => {
          if (product.body) {
            return of(product.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Product());
  }
}

@Injectable({ providedIn: 'root' })
export class ExternalTransferResolve implements Resolve<IStock> {
  constructor(private service: StockService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStock> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((stock: HttpResponse<Stock>) => {
          if (stock.body) {
            return of(stock.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Stock());
  }
}

const inventroyRoute: Routes = [
  {
    path: '',
    component: InventoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Simple Inventory',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/detail',
    component: InventoryProductDetailComponent,
    resolve: {
      product: InventoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Product Stock Details',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/internal',
    component: InventoryInternalTransferComponent,
    resolve: {
      product: InventoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Stock Internal Transfer',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'stock/:id/external',
    component: InventoryExternalTransferComponent,
    resolve: {
      stock: ExternalTransferResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Stock External Transfer',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  declarations: [
    InventoryComponent,
    InventoryProductDetailComponent,
    InventoryInternalTransferComponent,
    InventoryExternalTransferComponent,
  ],
  imports: [CommonModule, SimpleInventorySharedModule, RouterModule.forChild(inventroyRoute)],
})
export class ImportCsvFilesModule {}
