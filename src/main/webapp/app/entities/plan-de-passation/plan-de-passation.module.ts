import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SigmapSharedModule } from 'app/shared/shared.module';
import { PlanDePassationComponent } from './plan-de-passation.component';
import { PlanDePassationDetailComponent } from './plan-de-passation-detail.component';
import { PlanDePassationUpdateComponent } from './plan-de-passation-update.component';
import { PlanDePassationDeleteDialogComponent } from './plan-de-passation-delete-dialog.component';
import { planDePassationRoute } from './plan-de-passation.route';

@NgModule({
  imports: [SigmapSharedModule, RouterModule.forChild(planDePassationRoute)],
  declarations: [
    PlanDePassationComponent,
    PlanDePassationDetailComponent,
    PlanDePassationUpdateComponent,
    PlanDePassationDeleteDialogComponent,
  ],
  entryComponents: [PlanDePassationDeleteDialogComponent],
})
export class SigmapPlanDePassationModule {}
