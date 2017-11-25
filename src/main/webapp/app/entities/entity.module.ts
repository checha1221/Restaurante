import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RestauranteTprModule } from './tpr/tpr.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RestauranteTprModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestauranteEntityModule {}
