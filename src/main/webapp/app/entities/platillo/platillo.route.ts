import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PlatilloComponent } from './platillo.component';
import { PlatilloDetailComponent } from './platillo-detail.component';
import { PlatilloPopupComponent } from './platillo-dialog.component';
import { PlatilloDeletePopupComponent } from './platillo-delete-dialog.component';

@Injectable()
export class PlatilloResolvePagingParams implements Resolve<any> {

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

export const platilloRoute: Routes = [
    {
        path: 'platillo',
        component: PlatilloComponent,
        resolve: {
            'pagingParams': PlatilloResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.platillo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'platillo/:id',
        component: PlatilloDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.platillo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const platilloPopupRoute: Routes = [
    {
        path: 'platillo-new',
        component: PlatilloPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.platillo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'platillo/:id/edit',
        component: PlatilloPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.platillo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'platillo/:id/delete',
        component: PlatilloDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.platillo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
