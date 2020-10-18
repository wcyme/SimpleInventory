import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SimpleInventorySharedModule } from 'app/shared/shared.module';
import { InternalTransferLogComponent } from './internal-transfer-log.component';
import { InternalTransferLogDetailComponent } from './internal-transfer-log-detail.component';
import { internalTransferLogRoute } from './internal-transfer-log.route';

@NgModule({
  imports: [SimpleInventorySharedModule, RouterModule.forChild(internalTransferLogRoute)],
  declarations: [InternalTransferLogComponent, InternalTransferLogDetailComponent],
})
export class SimpleInventoryInternalTransferLogModule {}
