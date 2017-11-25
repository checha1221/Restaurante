import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Reserva } from './reserva.model';
import { ReservaPopupService } from './reserva-popup.service';
import { ReservaService } from './reserva.service';

@Component({
    selector: 'jhi-reserva-dialog',
    templateUrl: './reserva-dialog.component.html'
})
export class ReservaDialogComponent implements OnInit {

    reserva: Reserva;
    isSaving: boolean;
    fechaDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reservaService: ReservaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.reserva.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reservaService.update(this.reserva));
        } else {
            this.subscribeToSaveResponse(
                this.reservaService.create(this.reserva));
        }
    }

    private subscribeToSaveResponse(result: Observable<Reserva>) {
        result.subscribe((res: Reserva) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Reserva) {
        this.eventManager.broadcast({ name: 'reservaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-reserva-popup',
    template: ''
})
export class ReservaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reservaPopupService: ReservaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reservaPopupService
                    .open(ReservaDialogComponent as Component, params['id']);
            } else {
                this.reservaPopupService
                    .open(ReservaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
