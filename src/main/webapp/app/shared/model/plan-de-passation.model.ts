import { Moment } from 'moment';

export interface IPlanDePassation {
  id?: number;
  annee?: number;
  dateDebut?: Moment;
  dateFin?: Moment;
  commentaire?: string;
}

export class PlanDePassation implements IPlanDePassation {
  constructor(public id?: number, public annee?: number, public dateDebut?: Moment, public dateFin?: Moment, public commentaire?: string) {}
}
