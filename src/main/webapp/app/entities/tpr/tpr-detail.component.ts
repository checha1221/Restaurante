import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Tpr } from './tpr.model';
import { TprService } from './tpr.service';

@Component({
    selector: 'jhi-tpr-detail',
    templateUrl: './tpr-detail.component.html'
})
export class TprDetailComponent implements OnInit, OnDestroy {

    tpr: Tpr;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tprService: TprService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTprs();
    }

    load(id) {
        this.tprService.find(id).subscribe((tpr) => {
            this.tpr = tpr;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTprs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tprListModification',
            (response) => this.load(this.tpr.id)
        );
    }
}
