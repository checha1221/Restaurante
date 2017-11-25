import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Mesa } from './mesa.model';
import { MesaPopupService } from './mesa-popup.service';
import { MesaService } from './mesa.service';

@Component({
    selector: 'jhi-mesa-dialog',
    templateUrl: './mesa-dialog.component.html'
})
export class MesaDialogComponent implements OnInit {

    mesa: Mesa;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private mesaService: MesaService,
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
        if (this.mesa.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mesaService.update(this.mesa));
        } else {
            this.subscribeToSaveResponse(
                this.mesaService.create(this.mesa));
        }
    }

    private subscribeToSaveResponse(result: Observable<Mesa>) {
        result.subscribe((res: Mesa) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Mesa) {
        this.eventManager.broadcast({ name: 'mesaListModification', content: 'OK'});
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
    selector: 'jhi-mesa-popup',
    template: ''
})
export class MesaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mesaPopupService: MesaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mesaPopupService
                    .open(MesaDialogComponent as Component, params['id']);
            } else {
                this.mesaPopupService
                    .open(MesaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
