import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInternalTransferLog } from 'app/shared/model/internal-transfer-log.model';

type EntityResponseType = HttpResponse<IInternalTransferLog>;
type EntityArrayResponseType = HttpResponse<IInternalTransferLog[]>;

@Injectable({ providedIn: 'root' })
export class InternalTransferLogService {
  public resourceUrl = SERVER_API_URL + 'api/internal-transfer-logs';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInternalTransferLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInternalTransferLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(internalTransferLog: IInternalTransferLog): IInternalTransferLog {
    const copy: IInternalTransferLog = Object.assign({}, internalTransferLog, {
      createdDate:
        internalTransferLog.createdDate && internalTransferLog.createdDate.isValid() ? internalTransferLog.createdDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((internalTransferLog: IInternalTransferLog) => {
        internalTransferLog.createdDate = internalTransferLog.createdDate ? moment(internalTransferLog.createdDate) : undefined;
      });
    }
    return res;
  }
}
