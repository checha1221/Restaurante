import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RestauranteSharedModule } from '../../shared';
import {
    ReservaService,
    ReservaPopupService,
    ReservaComponent,
    ReservaDetailComponent,
    ReservaDialogComponent,
    ReservaPopupComponent,
    ReservaDeletePopupComponent,
    ReservaDeleteDialogComponent,
    reservaRoute,
    reservaPopupRoute,
    ReservaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...reservaRoute,
    ...reservaPopupRoute,
];

@NgModule({
    imports: [
        RestauranteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReservaComponent,
        ReservaDetailComponent,
        ReservaDialogComponent,
        ReservaDeleteDialogComponent,
        ReservaPopupComponent,
        ReservaDeletePopupComponent,
    ],
    entryComponents: [
        ReservaComponent,
        ReservaDialogComponent,
        ReservaPopupComponent,
        ReservaDeleteDialogComponent,
        ReservaDeletePopupComponent,
    ],
    providers: [
        ReservaService,
        ReservaPopupService,
        ReservaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestauranteReservaModule {}
