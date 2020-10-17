import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from '../app.constants';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<any>;

@Injectable({ providedIn: 'root' })
export class ImportCsvFilesService {
  public resourceUrl = SERVER_API_URL + 'api';

  constructor(protected http: HttpClient) {}

  importProducts(formData: FormData): Observable<EntityResponseType> {
    return this.http.post(`${this.resourceUrl + '/upload/products'}`, formData, { observe: 'response' });
  }

  importStocks(formData: FormData): Observable<EntityResponseType> {
    return this.http.post(`${this.resourceUrl + '/upload/stocks'}`, formData, { observe: 'response' });
  }
}
