/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RestauranteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PlatilloDetailComponent } from '../../../../../../main/webapp/app/entities/platillo/platillo-detail.component';
import { PlatilloService } from '../../../../../../main/webapp/app/entities/platillo/platillo.service';
import { Platillo } from '../../../../../../main/webapp/app/entities/platillo/platillo.model';

describe('Component Tests', () => {

    describe('Platillo Management Detail Component', () => {
        let comp: PlatilloDetailComponent;
        let fixture: ComponentFixture<PlatilloDetailComponent>;
        let service: PlatilloService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RestauranteTestModule],
                declarations: [PlatilloDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PlatilloService,
                    JhiEventManager
                ]
            }).overrideTemplate(PlatilloDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PlatilloDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlatilloService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Platillo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.platillo).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
