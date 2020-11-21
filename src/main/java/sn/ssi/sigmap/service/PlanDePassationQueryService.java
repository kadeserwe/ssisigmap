package sn.ssi.sigmap.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import sn.ssi.sigmap.domain.PlanDePassation;
import sn.ssi.sigmap.domain.*; // for static metamodels
import sn.ssi.sigmap.repository.PlanDePassationRepository;
import sn.ssi.sigmap.service.dto.PlanDePassationCriteria;
import sn.ssi.sigmap.service.dto.PlanDePassationDTO;
import sn.ssi.sigmap.service.mapper.PlanDePassationMapper;

/**
 * Service for executing complex queries for {@link PlanDePassation} entities in the database.
 * The main input is a {@link PlanDePassationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanDePassationDTO} or a {@link Page} of {@link PlanDePassationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanDePassationQueryService extends QueryService<PlanDePassation> {

    private final Logger log = LoggerFactory.getLogger(PlanDePassationQueryService.class);

    private final PlanDePassationRepository planDePassationRepository;

    private final PlanDePassationMapper planDePassationMapper;

    public PlanDePassationQueryService(PlanDePassationRepository planDePassationRepository, PlanDePassationMapper planDePassationMapper) {
        this.planDePassationRepository = planDePassationRepository;
        this.planDePassationMapper = planDePassationMapper;
    }

    /**
     * Return a {@link List} of {@link PlanDePassationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanDePassationDTO> findByCriteria(PlanDePassationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanDePassation> specification = createSpecification(criteria);
        return planDePassationMapper.toDto(planDePassationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanDePassationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanDePassationDTO> findByCriteria(PlanDePassationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanDePassation> specification = createSpecification(criteria);
        return planDePassationRepository.findAll(specification, page)
            .map(planDePassationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanDePassationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanDePassation> specification = createSpecification(criteria);
        return planDePassationRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanDePassationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlanDePassation> createSpecification(PlanDePassationCriteria criteria) {
        Specification<PlanDePassation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlanDePassation_.id));
            }
            if (criteria.getAnnee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnnee(), PlanDePassation_.annee));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), PlanDePassation_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), PlanDePassation_.dateFin));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), PlanDePassation_.commentaire));
            }
        }
        return specification;
    }
}
