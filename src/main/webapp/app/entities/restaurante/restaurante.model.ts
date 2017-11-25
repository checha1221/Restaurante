import { BaseEntity } from './../../shared';

export class Restaurante implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public direccion?: string,
        public telefono?: number,
        public status?: string,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
