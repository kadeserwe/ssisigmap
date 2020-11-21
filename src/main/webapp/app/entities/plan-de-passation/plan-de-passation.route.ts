import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlanDePassation, PlanDePassation } from 'app/shared/model/plan-de-passation.model';
import { PlanDePassationService } from './plan-de-passation.service';
import { PlanDePassationComponent } from './plan-de-passation.component';
import { PlanDePassationDetailComponent } from './plan-de-passation-detail.component';
import { PlanDePassationUpdateComponent } from './plan-de-passation-update.component';

@Injectable({ providedIn: 'root' })
export class PlanDePassationResolve implements Resolve<IPlanDePassation> {
  constructor(private service: PlanDePassationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlanDePassation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((planDePassation: HttpResponse<PlanDePassation>) => {
          if (planDePassation.body) {
            return of(planDePassation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlanDePassation());
  }
}

export const planDePassationRoute: Routes = [
  {
    path: '',
    component: PlanDePassationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'sigmapApp.planDePassation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanDePassationDetailComponent,
    resolve: {
      planDePassation: PlanDePassationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sigmapApp.planDePassation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanDePassationUpdateComponent,
    resolve: {
      planDePassation: PlanDePassationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sigmapApp.planDePassation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanDePassationUpdateComponent,
    resolve: {
      planDePassation: PlanDePassationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sigmapApp.planDePassation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
