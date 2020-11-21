import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SigmapTestModule } from '../../../test.module';
import { PlanDePassationUpdateComponent } from 'app/entities/plan-de-passation/plan-de-passation-update.component';
import { PlanDePassationService } from 'app/entities/plan-de-passation/plan-de-passation.service';
import { PlanDePassation } from 'app/shared/model/plan-de-passation.model';

describe('Component Tests', () => {
  describe('PlanDePassation Management Update Component', () => {
    let comp: PlanDePassationUpdateComponent;
    let fixture: ComponentFixture<PlanDePassationUpdateComponent>;
    let service: PlanDePassationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SigmapTestModule],
        declarations: [PlanDePassationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PlanDePassationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanDePassationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlanDePassationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlanDePassation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlanDePassation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
