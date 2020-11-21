import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlanDePassation } from 'app/shared/model/plan-de-passation.model';

type EntityResponseType = HttpResponse<IPlanDePassation>;
type EntityArrayResponseType = HttpResponse<IPlanDePassation[]>;

@Injectable({ providedIn: 'root' })
export class PlanDePassationService {
  public resourceUrl = SERVER_API_URL + 'api/plan-de-passations';

  constructor(protected http: HttpClient) {}

  create(planDePassation: IPlanDePassation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planDePassation);
    return this.http
      .post<IPlanDePassation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(planDePassation: IPlanDePassation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planDePassation);
    return this.http
      .put<IPlanDePassation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlanDePassation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlanDePassation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(planDePassation: IPlanDePassation): IPlanDePassation {
    const copy: IPlanDePassation = Object.assign({}, planDePassation, {
      dateDebut: planDePassation.dateDebut && planDePassation.dateDebut.isValid() ? planDePassation.dateDebut.toJSON() : undefined,
      dateFin: planDePassation.dateFin && planDePassation.dateFin.isValid() ? planDePassation.dateFin.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? moment(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? moment(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((planDePassation: IPlanDePassation) => {
        planDePassation.dateDebut = planDePassation.dateDebut ? moment(planDePassation.dateDebut) : undefined;
        planDePassation.dateFin = planDePassation.dateFin ? moment(planDePassation.dateFin) : undefined;
      });
    }
    return res;
  }
}
