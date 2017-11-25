import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Platillo } from './platillo.model';
import { PlatilloService } from './platillo.service';

@Component({
    selector: 'jhi-platillo-detail',
    templateUrl: './platillo-detail.component.html'
})
export class PlatilloDetailComponent implements OnInit, OnDestroy {

    platillo: Platillo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private platilloService: PlatilloService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPlatillos();
    }

    load(id) {
        this.platilloService.find(id).subscribe((platillo) => {
            this.platillo = platillo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPlatillos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'platilloListModification',
            (response) => this.load(this.platillo.id)
        );
    }
}
