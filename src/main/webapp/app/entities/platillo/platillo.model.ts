import { BaseEntity } from './../../shared';

export class Platillo implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public detalle?: string,
        public vencimiento?: any,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
