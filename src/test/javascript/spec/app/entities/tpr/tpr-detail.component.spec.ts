/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RestauranteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TprDetailComponent } from '../../../../../../main/webapp/app/entities/tpr/tpr-detail.component';
import { TprService } from '../../../../../../main/webapp/app/entities/tpr/tpr.service';
import { Tpr } from '../../../../../../main/webapp/app/entities/tpr/tpr.model';

describe('Component Tests', () => {

    describe('Tpr Management Detail Component', () => {
        let comp: TprDetailComponent;
        let fixture: ComponentFixture<TprDetailComponent>;
        let service: TprService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RestauranteTestModule],
                declarations: [TprDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TprService,
                    JhiEventManager
                ]
            }).overrideTemplate(TprDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TprDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TprService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Tpr(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tpr).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
