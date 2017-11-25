import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Platillo } from './platillo.model';
import { PlatilloPopupService } from './platillo-popup.service';
import { PlatilloService } from './platillo.service';

@Component({
    selector: 'jhi-platillo-delete-dialog',
    templateUrl: './platillo-delete-dialog.component.html'
})
export class PlatilloDeleteDialogComponent {

    platillo: Platillo;

    constructor(
        private platilloService: PlatilloService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.platilloService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'platilloListModification',
                content: 'Deleted an platillo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-platillo-delete-popup',
    template: ''
})
export class PlatilloDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private platilloPopupService: PlatilloPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.platilloPopupService
                .open(PlatilloDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
