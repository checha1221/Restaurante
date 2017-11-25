import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Platillo } from './platillo.model';
import { PlatilloPopupService } from './platillo-popup.service';
import { PlatilloService } from './platillo.service';

@Component({
    selector: 'jhi-platillo-dialog',
    templateUrl: './platillo-dialog.component.html'
})
export class PlatilloDialogComponent implements OnInit {

    platillo: Platillo;
    isSaving: boolean;
    vencimientoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private platilloService: PlatilloService,
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
        if (this.platillo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.platilloService.update(this.platillo));
        } else {
            this.subscribeToSaveResponse(
                this.platilloService.create(this.platillo));
        }
    }

    private subscribeToSaveResponse(result: Observable<Platillo>) {
        result.subscribe((res: Platillo) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Platillo) {
        this.eventManager.broadcast({ name: 'platilloListModification', content: 'OK'});
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
    selector: 'jhi-platillo-popup',
    template: ''
})
export class PlatilloPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private platilloPopupService: PlatilloPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.platilloPopupService
                    .open(PlatilloDialogComponent as Component, params['id']);
            } else {
                this.platilloPopupService
                    .open(PlatilloDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
