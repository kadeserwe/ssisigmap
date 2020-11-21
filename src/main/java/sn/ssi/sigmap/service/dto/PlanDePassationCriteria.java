package sn.ssi.sigmap.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link sn.ssi.sigmap.domain.PlanDePassation} entity. This class is used
 * in {@link sn.ssi.sigmap.web.rest.PlanDePassationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plan-de-passations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanDePassationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter annee;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private StringFilter commentaire;

    public PlanDePassationCriteria() {
    }

    public PlanDePassationCriteria(PlanDePassationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.annee = other.annee == null ? null : other.annee.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
    }

    @Override
    public PlanDePassationCriteria copy() {
        return new PlanDePassationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAnnee() {
        return annee;
    }

    public void setAnnee(IntegerFilter annee) {
        this.annee = annee;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanDePassationCriteria that = (PlanDePassationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(annee, that.annee) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(commentaire, that.commentaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        annee,
        dateDebut,
        dateFin,
        commentaire
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanDePassationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (annee != null ? "annee=" + annee + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            "}";
    }

}
