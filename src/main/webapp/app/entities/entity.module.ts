import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'plan-de-passation',
        loadChildren: () => import('./plan-de-passation/plan-de-passation.module').then(m => m.SigmapPlanDePassationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SigmapEntityModule {}
