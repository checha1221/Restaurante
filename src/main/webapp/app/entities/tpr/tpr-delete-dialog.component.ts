import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Tpr } from './tpr.model';
import { TprPopupService } from './tpr-popup.service';
import { TprService } from './tpr.service';

@Component({
    selector: 'jhi-tpr-delete-dialog',
    templateUrl: './tpr-delete-dialog.component.html'
})
export class TprDeleteDialogComponent {

    tpr: Tpr;

    constructor(
        private tprService: TprService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tprService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tprListModification',
                content: 'Deleted an tpr'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tpr-delete-popup',
    template: ''
})
export class TprDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tprPopupService: TprPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tprPopupService
                .open(TprDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
