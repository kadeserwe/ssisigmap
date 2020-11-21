import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanDePassation } from 'app/shared/model/plan-de-passation.model';
import { PlanDePassationService } from './plan-de-passation.service';

@Component({
  templateUrl: './plan-de-passation-delete-dialog.component.html',
})
export class PlanDePassationDeleteDialogComponent {
  planDePassation?: IPlanDePassation;

  constructor(
    protected planDePassationService: PlanDePassationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planDePassationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('planDePassationListModification');
      this.activeModal.close();
    });
  }
}
