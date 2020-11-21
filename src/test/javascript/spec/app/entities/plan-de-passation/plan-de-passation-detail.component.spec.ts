import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SigmapTestModule } from '../../../test.module';
import { PlanDePassationDetailComponent } from 'app/entities/plan-de-passation/plan-de-passation-detail.component';
import { PlanDePassation } from 'app/shared/model/plan-de-passation.model';

describe('Component Tests', () => {
  describe('PlanDePassation Management Detail Component', () => {
    let comp: PlanDePassationDetailComponent;
    let fixture: ComponentFixture<PlanDePassationDetailComponent>;
    const route = ({ data: of({ planDePassation: new PlanDePassation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SigmapTestModule],
        declarations: [PlanDePassationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PlanDePassationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlanDePassationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load planDePassation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.planDePassation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
