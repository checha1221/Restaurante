import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Reserva } from './reserva.model';
import { ReservaService } from './reserva.service';

@Injectable()
export class ReservaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private reservaService: ReservaService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.reservaService.find(id).subscribe((reserva) => {
                    if (reserva.fecha) {
                        reserva.fecha = {
                            year: reserva.fecha.getFullYear(),
                            month: reserva.fecha.getMonth() + 1,
                            day: reserva.fecha.getDate()
                        };
                    }
                    this.ngbModalRef = this.reservaModalRef(component, reserva);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.reservaModalRef(component, new Reserva());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    reservaModalRef(component: Component, reserva: Reserva): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.reserva = reserva;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
