import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RestauranteComponent } from './restaurante.component';
import { RestauranteDetailComponent } from './restaurante-detail.component';
import { RestaurantePopupComponent } from './restaurante-dialog.component';
import { RestauranteDeletePopupComponent } from './restaurante-delete-dialog.component';

@Injectable()
export class RestauranteResolvePagingParams implements Resolve<any> {

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

export const restauranteRoute: Routes = [
    {
        path: 'restaurante',
        component: RestauranteComponent,
        resolve: {
            'pagingParams': RestauranteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.restaurante.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'restaurante/:id',
        component: RestauranteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.restaurante.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const restaurantePopupRoute: Routes = [
    {
        path: 'restaurante-new',
        component: RestaurantePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.restaurante.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'restaurante/:id/edit',
        component: RestaurantePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.restaurante.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'restaurante/:id/delete',
        component: RestauranteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.restaurante.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
