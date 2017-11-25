import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Mesa } from './mesa.model';
import { MesaService } from './mesa.service';

@Component({
    selector: 'jhi-mesa-detail',
    templateUrl: './mesa-detail.component.html'
})
export class MesaDetailComponent implements OnInit, OnDestroy {

    mesa: Mesa;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mesaService: MesaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMesas();
    }

    load(id) {
        this.mesaService.find(id).subscribe((mesa) => {
            this.mesa = mesa;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMesas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mesaListModification',
            (response) => this.load(this.mesa.id)
        );
    }
}
