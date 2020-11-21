package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.service.PlanDePassationService;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import sn.ssi.sigmap.service.dto.PlanDePassationDTO;
import sn.ssi.sigmap.service.dto.PlanDePassationCriteria;
import sn.ssi.sigmap.service.PlanDePassationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.PlanDePassation}.
 */
@RestController
@RequestMapping("/api")
public class PlanDePassationResource {

    private final Logger log = LoggerFactory.getLogger(PlanDePassationResource.class);

    private static final String ENTITY_NAME = "planDePassation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanDePassationService planDePassationService;

    private final PlanDePassationQueryService planDePassationQueryService;

    public PlanDePassationResource(PlanDePassationService planDePassationService, PlanDePassationQueryService planDePassationQueryService) {
        this.planDePassationService = planDePassationService;
        this.planDePassationQueryService = planDePassationQueryService;
    }

    /**
     * {@code POST  /plan-de-passations} : Create a new planDePassation.
     *
     * @param planDePassationDTO the planDePassationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planDePassationDTO, or with status {@code 400 (Bad Request)} if the planDePassation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plan-de-passations")
    public ResponseEntity<PlanDePassationDTO> createPlanDePassation(@RequestBody PlanDePassationDTO planDePassationDTO) throws URISyntaxException {
        log.debug("REST request to save PlanDePassation : {}", planDePassationDTO);
        if (planDePassationDTO.getId() != null) {
            throw new BadRequestAlertException("A new planDePassation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanDePassationDTO result = planDePassationService.save(planDePassationDTO);
        return ResponseEntity.created(new URI("/api/plan-de-passations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-de-passations} : Updates an existing planDePassation.
     *
     * @param planDePassationDTO the planDePassationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planDePassationDTO,
     * or with status {@code 400 (Bad Request)} if the planDePassationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planDePassationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plan-de-passations")
    public ResponseEntity<PlanDePassationDTO> updatePlanDePassation(@RequestBody PlanDePassationDTO planDePassationDTO) throws URISyntaxException {
        log.debug("REST request to update PlanDePassation : {}", planDePassationDTO);
        if (planDePassationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanDePassationDTO result = planDePassationService.save(planDePassationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planDePassationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plan-de-passations} : get all the planDePassations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planDePassations in body.
     */
    @GetMapping("/plan-de-passations")
    public ResponseEntity<List<PlanDePassationDTO>> getAllPlanDePassations(PlanDePassationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlanDePassations by criteria: {}", criteria);
        Page<PlanDePassationDTO> page = planDePassationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-de-passations/count} : count all the planDePassations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/plan-de-passations/count")
    public ResponseEntity<Long> countPlanDePassations(PlanDePassationCriteria criteria) {
        log.debug("REST request to count PlanDePassations by criteria: {}", criteria);
        return ResponseEntity.ok().body(planDePassationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plan-de-passations/:id} : get the "id" planDePassation.
     *
     * @param id the id of the planDePassationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planDePassationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plan-de-passations/{id}")
    public ResponseEntity<PlanDePassationDTO> getPlanDePassation(@PathVariable Long id) {
        log.debug("REST request to get PlanDePassation : {}", id);
        Optional<PlanDePassationDTO> planDePassationDTO = planDePassationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planDePassationDTO);
    }

    /**
     * {@code DELETE  /plan-de-passations/:id} : delete the "id" planDePassation.
     *
     * @param id the id of the planDePassationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plan-de-passations/{id}")
    public ResponseEntity<Void> deletePlanDePassation(@PathVariable Long id) {
        log.debug("REST request to delete PlanDePassation : {}", id);
        planDePassationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
