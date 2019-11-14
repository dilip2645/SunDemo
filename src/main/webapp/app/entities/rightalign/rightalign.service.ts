import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRightalign } from 'app/shared/model/rightalign.model';

type EntityResponseType = HttpResponse<IRightalign>;
type EntityArrayResponseType = HttpResponse<IRightalign[]>;

@Injectable({ providedIn: 'root' })
export class RightalignService {
  public resourceUrl = SERVER_API_URL + 'api/rightaligns';

  constructor(protected http: HttpClient) {}

  create(rightalign: IRightalign): Observable<EntityResponseType> {
    return this.http.post<IRightalign>(this.resourceUrl, rightalign, { observe: 'response' });
  }

  update(rightalign: IRightalign): Observable<EntityResponseType> {
    return this.http.put<IRightalign>(this.resourceUrl, rightalign, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRightalign>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRightalign[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  exportReport(type: string, req?: any): Observable<any> {
    return Observable.create(observer => {
      const options = createRequestOption(req);
      this.http
        .get(this.resourceUrl + '?exportType=' + type.toLowerCase(), {
          params: options,
          observe: 'response',
          responseType: 'blob'
        })
        .subscribe(
          response => {
            observer.next(response);
            observer.complete();
          },
          error => {
            observer.error(error);
            observer.complete();
          }
        );
    });
  }
}
