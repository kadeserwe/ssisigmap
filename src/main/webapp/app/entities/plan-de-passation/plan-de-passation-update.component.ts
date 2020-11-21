import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPlanDePassation, PlanDePassation } from 'app/shared/model/plan-de-passation.model';
import { PlanDePassationService } from './plan-de-passation.service';

@Component({
  selector: 'jhi-plan-de-passation-update',
  templateUrl: './plan-de-passation-update.component.html',
})
export class PlanDePassationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    annee: [],
    dateDebut: [],
    dateFin: [],
    commentaire: [],
  });

  constructor(
    protected planDePassationService: PlanDePassationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planDePassation }) => {
      if (!planDePassation.id) {
        const today = moment().startOf('day');
        planDePassation.dateDebut = today;
        planDePassation.dateFin = today;
      }

      this.updateForm(planDePassation);
    });
  }

  updateForm(planDePassation: IPlanDePassation): void {
    this.editForm.patchValue({
      id: planDePassation.id,
      annee: planDePassation.annee,
      dateDebut: planDePassation.dateDebut ? planDePassation.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: planDePassation.dateFin ? planDePassation.dateFin.format(DATE_TIME_FORMAT) : null,
      commentaire: planDePassation.commentaire,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planDePassation = this.createFromForm();
    if (planDePassation.id !== undefined) {
      this.subscribeToSaveResponse(this.planDePassationService.update(planDePassation));
    } else {
      this.subscribeToSaveResponse(this.planDePassationService.create(planDePassation));
    }
  }

  private createFromForm(): IPlanDePassation {
    return {
      ...new PlanDePassation(),
      id: this.editForm.get(['id'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value ? moment(this.editForm.get(['dateDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin'])!.value ? moment(this.editForm.get(['dateFin'])!.value, DATE_TIME_FORMAT) : undefined,
      commentaire: this.editForm.get(['commentaire'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanDePassation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
