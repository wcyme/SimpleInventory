import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInternalTransferLog, InternalTransferLog } from 'app/shared/model/internal-transfer-log.model';
import { InternalTransferLogService } from './internal-transfer-log.service';
import { InternalTransferLogComponent } from './internal-transfer-log.component';
import { InternalTransferLogDetailComponent } from './internal-transfer-log-detail.component';

@Injectable({ providedIn: 'root' })
export class InternalTransferLogResolve implements Resolve<IInternalTransferLog> {
  constructor(private service: InternalTransferLogService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInternalTransferLog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((internalTransferLog: HttpResponse<InternalTransferLog>) => {
          if (internalTransferLog.body) {
            return of(internalTransferLog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InternalTransferLog());
  }
}

export const internalTransferLogRoute: Routes = [
  {
    path: '',
    component: InternalTransferLogComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'createdDate,desc',
      pageTitle: 'InternalTransferLogs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InternalTransferLogDetailComponent,
    resolve: {
      internalTransferLog: InternalTransferLogResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InternalTransferLogs',
    },
    canActivate: [UserRouteAccessService],
  },
];
