import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Reserva } from './reserva.model';
import { ReservaService } from './reserva.service';

@Component({
    selector: 'jhi-reserva-detail',
    templateUrl: './reserva-detail.component.html'
})
export class ReservaDetailComponent implements OnInit, OnDestroy {

    reserva: Reserva;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reservaService: ReservaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReservas();
    }

    load(id) {
        this.reservaService.find(id).subscribe((reserva) => {
            this.reserva = reserva;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReservas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reservaListModification',
            (response) => this.load(this.reserva.id)
        );
    }
}
