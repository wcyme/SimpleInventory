import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.SimpleInventoryProductModule),
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.SimpleInventoryStockModule),
      },
      {
        path: 'log/internal-transfer',
        loadChildren: () =>
          import('./internal-transfer-log/internal-transfer-log.module').then(m => m.SimpleInventoryInternalTransferLogModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SimpleInventoryEntityModule {}
