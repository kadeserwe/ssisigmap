import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanDePassation } from 'app/shared/model/plan-de-passation.model';

@Component({
  selector: 'jhi-plan-de-passation-detail',
  templateUrl: './plan-de-passation-detail.component.html',
})
export class PlanDePassationDetailComponent implements OnInit {
  planDePassation: IPlanDePassation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planDePassation }) => (this.planDePassation = planDePassation));
  }

  previousState(): void {
    window.history.back();
  }
}
