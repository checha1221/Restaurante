import { BaseEntity } from './../../shared';

export class Mesa implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public ubicacion?: string,
        public area?: string,
        public capacidad?: number,
        public estado?: string,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
