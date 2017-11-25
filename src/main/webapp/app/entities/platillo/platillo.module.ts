import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RestauranteSharedModule } from '../../shared';
import {
    PlatilloService,
    PlatilloPopupService,
    PlatilloComponent,
    PlatilloDetailComponent,
    PlatilloDialogComponent,
    PlatilloPopupComponent,
    PlatilloDeletePopupComponent,
    PlatilloDeleteDialogComponent,
    platilloRoute,
    platilloPopupRoute,
    PlatilloResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...platilloRoute,
    ...platilloPopupRoute,
];

@NgModule({
    imports: [
        RestauranteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PlatilloComponent,
        PlatilloDetailComponent,
        PlatilloDialogComponent,
        PlatilloDeleteDialogComponent,
        PlatilloPopupComponent,
        PlatilloDeletePopupComponent,
    ],
    entryComponents: [
        PlatilloComponent,
        PlatilloDialogComponent,
        PlatilloPopupComponent,
        PlatilloDeleteDialogComponent,
        PlatilloDeletePopupComponent,
    ],
    providers: [
        PlatilloService,
        PlatilloPopupService,
        PlatilloResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestaurantePlatilloModule {}
