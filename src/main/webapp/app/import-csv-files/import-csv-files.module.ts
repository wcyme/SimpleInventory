import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImportCsvFilesComponent } from './import-csv-files.component';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';

const importCsvFilesRoute: Routes = [
  {
    path: '',
    component: ImportCsvFilesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Import CSV Data',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  declarations: [ImportCsvFilesComponent],
  imports: [CommonModule, RouterModule.forChild(importCsvFilesRoute)],
})
export class ImportCsvFilesModule {}
