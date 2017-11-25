import { BaseEntity } from './../../shared';

export class Tpr implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public direccion?: string,
        public status?: string,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
