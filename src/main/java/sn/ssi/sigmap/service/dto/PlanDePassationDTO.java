package sn.ssi.sigmap.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link sn.ssi.sigmap.domain.PlanDePassation} entity.
 */
public class PlanDePassationDTO implements Serializable {
    
    private Long id;

    private Integer annee;

    private Instant dateDebut;

    private Instant dateFin;

    private String commentaire;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanDePassationDTO)) {
            return false;
        }

        return id != null && id.equals(((PlanDePassationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanDePassationDTO{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
