/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RestauranteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReservaDetailComponent } from '../../../../../../main/webapp/app/entities/reserva/reserva-detail.component';
import { ReservaService } from '../../../../../../main/webapp/app/entities/reserva/reserva.service';
import { Reserva } from '../../../../../../main/webapp/app/entities/reserva/reserva.model';

describe('Component Tests', () => {

    describe('Reserva Management Detail Component', () => {
        let comp: ReservaDetailComponent;
        let fixture: ComponentFixture<ReservaDetailComponent>;
        let service: ReservaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RestauranteTestModule],
                declarations: [ReservaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReservaService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReservaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReservaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Reserva(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.reserva).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
