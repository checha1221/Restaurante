import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RestauranteSharedModule } from '../../shared';
import {
    TprService,
    TprPopupService,
    TprComponent,
    TprDetailComponent,
    TprDialogComponent,
    TprPopupComponent,
    TprDeletePopupComponent,
    TprDeleteDialogComponent,
    tprRoute,
    tprPopupRoute,
    TprResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tprRoute,
    ...tprPopupRoute,
];

@NgModule({
    imports: [
        RestauranteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TprComponent,
        TprDetailComponent,
        TprDialogComponent,
        TprDeleteDialogComponent,
        TprPopupComponent,
        TprDeletePopupComponent,
    ],
    entryComponents: [
        TprComponent,
        TprDialogComponent,
        TprPopupComponent,
        TprDeleteDialogComponent,
        TprDeletePopupComponent,
    ],
    providers: [
        TprService,
        TprPopupService,
        TprResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestauranteTprModule {}
