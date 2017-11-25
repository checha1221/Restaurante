import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TprComponent } from './tpr.component';
import { TprDetailComponent } from './tpr-detail.component';
import { TprPopupComponent } from './tpr-dialog.component';
import { TprDeletePopupComponent } from './tpr-delete-dialog.component';

@Injectable()
export class TprResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const tprRoute: Routes = [
    {
        path: 'tpr',
        component: TprComponent,
        resolve: {
            'pagingParams': TprResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.tpr.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tpr/:id',
        component: TprDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.tpr.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tprPopupRoute: Routes = [
    {
        path: 'tpr-new',
        component: TprPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.tpr.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tpr/:id/edit',
        component: TprPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.tpr.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tpr/:id/delete',
        component: TprDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.tpr.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
