import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Restaurante } from './restaurante.model';
import { RestaurantePopupService } from './restaurante-popup.service';
import { RestauranteService } from './restaurante.service';

@Component({
    selector: 'jhi-restaurante-dialog',
    templateUrl: './restaurante-dialog.component.html'
})
export class RestauranteDialogComponent implements OnInit {

    restaurante: Restaurante;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private restauranteService: RestauranteService,
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
        if (this.restaurante.id !== undefined) {
            this.subscribeToSaveResponse(
                this.restauranteService.update(this.restaurante));
        } else {
            this.subscribeToSaveResponse(
                this.restauranteService.create(this.restaurante));
        }
    }

    private subscribeToSaveResponse(result: Observable<Restaurante>) {
        result.subscribe((res: Restaurante) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Restaurante) {
        this.eventManager.broadcast({ name: 'restauranteListModification', content: 'OK'});
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
    selector: 'jhi-restaurante-popup',
    template: ''
})
export class RestaurantePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private restaurantePopupService: RestaurantePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.restaurantePopupService
                    .open(RestauranteDialogComponent as Component, params['id']);
            } else {
                this.restaurantePopupService
                    .open(RestauranteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
