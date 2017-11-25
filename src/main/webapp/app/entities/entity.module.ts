import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RestauranteTprModule } from './tpr/tpr.module';
import { RestauranteRestauranteModule } from './restaurante/restaurante.module';
import { RestauranteMesaModule } from './mesa/mesa.module';
import { RestaurantePlatilloModule } from './platillo/platillo.module';
import { RestauranteProductoModule } from './producto/producto.module';
import { RestauranteReservaModule } from './reserva/reserva.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RestauranteTprModule,
        RestauranteRestauranteModule,
        RestauranteMesaModule,
        RestaurantePlatilloModule,
        RestauranteProductoModule,
        RestauranteReservaModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestauranteEntityModule {}
