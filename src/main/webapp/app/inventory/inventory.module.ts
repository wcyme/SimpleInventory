import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { SimpleInventorySharedModule } from 'app/shared/shared.module';
import { InventoryComponent } from './inventory.component';

const importCsvFilesRoute: Routes = [
  {
    path: '',
    component: InventoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Simple Inventory',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  declarations: [InventoryComponent],
  imports: [CommonModule, SimpleInventorySharedModule, RouterModule.forChild(importCsvFilesRoute)],
})
export class ImportCsvFilesModule {}
