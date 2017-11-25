import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Platillo } from './platillo.model';
import { PlatilloService } from './platillo.service';

@Injectable()
export class PlatilloPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private platilloService: PlatilloService

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
                this.platilloService.find(id).subscribe((platillo) => {
                    if (platillo.vencimiento) {
                        platillo.vencimiento = {
                            year: platillo.vencimiento.getFullYear(),
                            month: platillo.vencimiento.getMonth() + 1,
                            day: platillo.vencimiento.getDate()
                        };
                    }
                    this.ngbModalRef = this.platilloModalRef(component, platillo);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.platilloModalRef(component, new Platillo());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    platilloModalRef(component: Component, platillo: Platillo): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.platillo = platillo;
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
