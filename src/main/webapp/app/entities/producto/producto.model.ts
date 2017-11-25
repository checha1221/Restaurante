import { BaseEntity } from './../../shared';

export class Producto implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public cantidad?: number,
        public vencimiento?: any,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
