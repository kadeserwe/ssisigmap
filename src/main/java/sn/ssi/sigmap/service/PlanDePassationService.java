package sn.ssi.sigmap.service;

import sn.ssi.sigmap.domain.PlanDePassation;
import sn.ssi.sigmap.repository.PlanDePassationRepository;
import sn.ssi.sigmap.service.dto.PlanDePassationDTO;
import sn.ssi.sigmap.service.mapper.PlanDePassationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PlanDePassation}.
 */
@Service
@Transactional
public class PlanDePassationService {

    private final Logger log = LoggerFactory.getLogger(PlanDePassationService.class);

    private final PlanDePassationRepository planDePassationRepository;

    private final PlanDePassationMapper planDePassationMapper;

    public PlanDePassationService(PlanDePassationRepository planDePassationRepository, PlanDePassationMapper planDePassationMapper) {
        this.planDePassationRepository = planDePassationRepository;
        this.planDePassationMapper = planDePassationMapper;
    }

    /**
     * Save a planDePassation.
     *
     * @param planDePassationDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanDePassationDTO save(PlanDePassationDTO planDePassationDTO) {
        log.debug("Request to save PlanDePassation : {}", planDePassationDTO);
        PlanDePassation planDePassation = planDePassationMapper.toEntity(planDePassationDTO);
        planDePassation = planDePassationRepository.save(planDePassation);
        return planDePassationMapper.toDto(planDePassation);
    }

    /**
     * Get all the planDePassations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanDePassationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanDePassations");
        return planDePassationRepository.findAll(pageable)
            .map(planDePassationMapper::toDto);
    }


    /**
     * Get one planDePassation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanDePassationDTO> findOne(Long id) {
        log.debug("Request to get PlanDePassation : {}", id);
        return planDePassationRepository.findById(id)
            .map(planDePassationMapper::toDto);
    }

    /**
     * Delete the planDePassation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanDePassation : {}", id);
        planDePassationRepository.deleteById(id);
    }
}
