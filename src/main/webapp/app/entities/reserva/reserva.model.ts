import { BaseEntity } from './../../shared';

export class Reserva implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public fecha?: any,
        public comensales?: number,
        public area?: string,
        public activo?: boolean,
    ) {
        this.activo = false;
    }
}
