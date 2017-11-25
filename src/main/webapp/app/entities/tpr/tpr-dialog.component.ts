import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tpr } from './tpr.model';
import { TprPopupService } from './tpr-popup.service';
import { TprService } from './tpr.service';

@Component({
    selector: 'jhi-tpr-dialog',
    templateUrl: './tpr-dialog.component.html'
})
export class TprDialogComponent implements OnInit {

    tpr: Tpr;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tprService: TprService,
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
        if (this.tpr.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tprService.update(this.tpr));
        } else {
            this.subscribeToSaveResponse(
                this.tprService.create(this.tpr));
        }
    }

    private subscribeToSaveResponse(result: Observable<Tpr>) {
        result.subscribe((res: Tpr) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Tpr) {
        this.eventManager.broadcast({ name: 'tprListModification', content: 'OK'});
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
    selector: 'jhi-tpr-popup',
    template: ''
})
export class TprPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tprPopupService: TprPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tprPopupService
                    .open(TprDialogComponent as Component, params['id']);
            } else {
                this.tprPopupService
                    .open(TprDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
