import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RestauranteTprModule } from './tpr/tpr.module';
import { RestauranteRestauranteModule } from './restaurante/restaurante.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RestauranteTprModule,
        RestauranteRestauranteModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestauranteEntityModule {}
