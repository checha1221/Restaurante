import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReservaComponent } from './reserva.component';
import { ReservaDetailComponent } from './reserva-detail.component';
import { ReservaPopupComponent } from './reserva-dialog.component';
import { ReservaDeletePopupComponent } from './reserva-delete-dialog.component';

@Injectable()
export class ReservaResolvePagingParams implements Resolve<any> {

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

export const reservaRoute: Routes = [
    {
        path: 'reserva',
        component: ReservaComponent,
        resolve: {
            'pagingParams': ReservaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.reserva.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'reserva/:id',
        component: ReservaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.reserva.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reservaPopupRoute: Routes = [
    {
        path: 'reserva-new',
        component: ReservaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.reserva.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reserva/:id/edit',
        component: ReservaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.reserva.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reserva/:id/delete',
        component: ReservaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'restauranteApp.reserva.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
