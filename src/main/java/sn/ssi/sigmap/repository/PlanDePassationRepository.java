package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.PlanDePassation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PlanDePassation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanDePassationRepository extends JpaRepository<PlanDePassation, Long>, JpaSpecificationExecutor<PlanDePassation> {
}
