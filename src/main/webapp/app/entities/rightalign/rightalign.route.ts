import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Rightalign } from 'app/shared/model/rightalign.model';
import { RightalignService } from './rightalign.service';
import { RightalignListComponent } from './rightalign-list.component';
import { RightalignUpdateComponent } from './rightalign-update.component';
import { IRightalign } from 'app/shared/model/rightalign.model';

@Injectable({ providedIn: 'root' })
export class RightalignResolve implements Resolve<IRightalign> {
  constructor(private service: RightalignService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRightalign> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Rightalign>) => response.ok),
        map((rightalign: HttpResponse<Rightalign>) => rightalign.body)
      );
    }
    return of(new Rightalign());
  }
}

export const rightalignRoute: Routes = [
  {
    path: '',
    component: RightalignListComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Rightaligns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rightalign-new',
    component: RightalignUpdateComponent,
    resolve: {
      rightalign: RightalignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Rightaligns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RightalignUpdateComponent,
    resolve: {
      rightalign: RightalignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Rightaligns'
    },
    canActivate: [UserRouteAccessService]
  }
];
