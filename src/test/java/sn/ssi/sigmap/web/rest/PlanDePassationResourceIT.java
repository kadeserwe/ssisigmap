package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.SigmapApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.PlanDePassation;
import sn.ssi.sigmap.repository.PlanDePassationRepository;
import sn.ssi.sigmap.service.PlanDePassationService;
import sn.ssi.sigmap.service.dto.PlanDePassationDTO;
import sn.ssi.sigmap.service.mapper.PlanDePassationMapper;
import sn.ssi.sigmap.service.dto.PlanDePassationCriteria;
import sn.ssi.sigmap.service.PlanDePassationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlanDePassationResource} REST controller.
 */
@SpringBootTest(classes = { SigmapApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PlanDePassationResourceIT {

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;
    private static final Integer SMALLER_ANNEE = 1 - 1;

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    @Autowired
    private PlanDePassationRepository planDePassationRepository;

    @Autowired
    private PlanDePassationMapper planDePassationMapper;

    @Autowired
    private PlanDePassationService planDePassationService;

    @Autowired
    private PlanDePassationQueryService planDePassationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanDePassationMockMvc;

    private PlanDePassation planDePassation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanDePassation createEntity(EntityManager em) {
        PlanDePassation planDePassation = new PlanDePassation()
            .annee(DEFAULT_ANNEE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .commentaire(DEFAULT_COMMENTAIRE);
        return planDePassation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanDePassation createUpdatedEntity(EntityManager em) {
        PlanDePassation planDePassation = new PlanDePassation()
            .annee(UPDATED_ANNEE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);
        return planDePassation;
    }

    @BeforeEach
    public void initTest() {
        planDePassation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanDePassation() throws Exception {
        int databaseSizeBeforeCreate = planDePassationRepository.findAll().size();
        // Create the PlanDePassation
        PlanDePassationDTO planDePassationDTO = planDePassationMapper.toDto(planDePassation);
        restPlanDePassationMockMvc.perform(post("/api/plan-de-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planDePassationDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanDePassation in the database
        List<PlanDePassation> planDePassationList = planDePassationRepository.findAll();
        assertThat(planDePassationList).hasSize(databaseSizeBeforeCreate + 1);
        PlanDePassation testPlanDePassation = planDePassationList.get(planDePassationList.size() - 1);
        assertThat(testPlanDePassation.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testPlanDePassation.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPlanDePassation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPlanDePassation.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void createPlanDePassationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planDePassationRepository.findAll().size();

        // Create the PlanDePassation with an existing ID
        planDePassation.setId(1L);
        PlanDePassationDTO planDePassationDTO = planDePassationMapper.toDto(planDePassation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanDePassationMockMvc.perform(post("/api/plan-de-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planDePassationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanDePassation in the database
        List<PlanDePassation> planDePassationList = planDePassationRepository.findAll();
        assertThat(planDePassationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlanDePassations() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planDePassation.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }
    
    @Test
    @Transactional
    public void getPlanDePassation() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get the planDePassation
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations/{id}", planDePassation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planDePassation.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }


    @Test
    @Transactional
    public void getPlanDePassationsByIdFiltering() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        Long id = planDePassation.getId();

        defaultPlanDePassationShouldBeFound("id.equals=" + id);
        defaultPlanDePassationShouldNotBeFound("id.notEquals=" + id);

        defaultPlanDePassationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlanDePassationShouldNotBeFound("id.greaterThan=" + id);

        defaultPlanDePassationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlanDePassationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee equals to DEFAULT_ANNEE
        defaultPlanDePassationShouldBeFound("annee.equals=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee equals to UPDATED_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.equals=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee not equals to DEFAULT_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.notEquals=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee not equals to UPDATED_ANNEE
        defaultPlanDePassationShouldBeFound("annee.notEquals=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsInShouldWork() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee in DEFAULT_ANNEE or UPDATED_ANNEE
        defaultPlanDePassationShouldBeFound("annee.in=" + DEFAULT_ANNEE + "," + UPDATED_ANNEE);

        // Get all the planDePassationList where annee equals to UPDATED_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.in=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee is not null
        defaultPlanDePassationShouldBeFound("annee.specified=true");

        // Get all the planDePassationList where annee is null
        defaultPlanDePassationShouldNotBeFound("annee.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee is greater than or equal to DEFAULT_ANNEE
        defaultPlanDePassationShouldBeFound("annee.greaterThanOrEqual=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee is greater than or equal to UPDATED_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.greaterThanOrEqual=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee is less than or equal to DEFAULT_ANNEE
        defaultPlanDePassationShouldBeFound("annee.lessThanOrEqual=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee is less than or equal to SMALLER_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.lessThanOrEqual=" + SMALLER_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsLessThanSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee is less than DEFAULT_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.lessThan=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee is less than UPDATED_ANNEE
        defaultPlanDePassationShouldBeFound("annee.lessThan=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByAnneeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where annee is greater than DEFAULT_ANNEE
        defaultPlanDePassationShouldNotBeFound("annee.greaterThan=" + DEFAULT_ANNEE);

        // Get all the planDePassationList where annee is greater than SMALLER_ANNEE
        defaultPlanDePassationShouldBeFound("annee.greaterThan=" + SMALLER_ANNEE);
    }


    @Test
    @Transactional
    public void getAllPlanDePassationsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultPlanDePassationShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the planDePassationList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultPlanDePassationShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateDebut not equals to DEFAULT_DATE_DEBUT
        defaultPlanDePassationShouldNotBeFound("dateDebut.notEquals=" + DEFAULT_DATE_DEBUT);

        // Get all the planDePassationList where dateDebut not equals to UPDATED_DATE_DEBUT
        defaultPlanDePassationShouldBeFound("dateDebut.notEquals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultPlanDePassationShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the planDePassationList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultPlanDePassationShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateDebut is not null
        defaultPlanDePassationShouldBeFound("dateDebut.specified=true");

        // Get all the planDePassationList where dateDebut is null
        defaultPlanDePassationShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateFin equals to DEFAULT_DATE_FIN
        defaultPlanDePassationShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the planDePassationList where dateFin equals to UPDATED_DATE_FIN
        defaultPlanDePassationShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateFin not equals to DEFAULT_DATE_FIN
        defaultPlanDePassationShouldNotBeFound("dateFin.notEquals=" + DEFAULT_DATE_FIN);

        // Get all the planDePassationList where dateFin not equals to UPDATED_DATE_FIN
        defaultPlanDePassationShouldBeFound("dateFin.notEquals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultPlanDePassationShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the planDePassationList where dateFin equals to UPDATED_DATE_FIN
        defaultPlanDePassationShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where dateFin is not null
        defaultPlanDePassationShouldBeFound("dateFin.specified=true");

        // Get all the planDePassationList where dateFin is null
        defaultPlanDePassationShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultPlanDePassationShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the planDePassationList where commentaire equals to UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire not equals to DEFAULT_COMMENTAIRE
        defaultPlanDePassationShouldNotBeFound("commentaire.notEquals=" + DEFAULT_COMMENTAIRE);

        // Get all the planDePassationList where commentaire not equals to UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldBeFound("commentaire.notEquals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the planDePassationList where commentaire equals to UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire is not null
        defaultPlanDePassationShouldBeFound("commentaire.specified=true");

        // Get all the planDePassationList where commentaire is null
        defaultPlanDePassationShouldNotBeFound("commentaire.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire contains DEFAULT_COMMENTAIRE
        defaultPlanDePassationShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the planDePassationList where commentaire contains UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void getAllPlanDePassationsByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        // Get all the planDePassationList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultPlanDePassationShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the planDePassationList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultPlanDePassationShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanDePassationShouldBeFound(String filter) throws Exception {
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planDePassation.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanDePassationShouldNotBeFound(String filter) throws Exception {
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPlanDePassation() throws Exception {
        // Get the planDePassation
        restPlanDePassationMockMvc.perform(get("/api/plan-de-passations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanDePassation() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        int databaseSizeBeforeUpdate = planDePassationRepository.findAll().size();

        // Update the planDePassation
        PlanDePassation updatedPlanDePassation = planDePassationRepository.findById(planDePassation.getId()).get();
        // Disconnect from session so that the updates on updatedPlanDePassation are not directly saved in db
        em.detach(updatedPlanDePassation);
        updatedPlanDePassation
            .annee(UPDATED_ANNEE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);
        PlanDePassationDTO planDePassationDTO = planDePassationMapper.toDto(updatedPlanDePassation);

        restPlanDePassationMockMvc.perform(put("/api/plan-de-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planDePassationDTO)))
            .andExpect(status().isOk());

        // Validate the PlanDePassation in the database
        List<PlanDePassation> planDePassationList = planDePassationRepository.findAll();
        assertThat(planDePassationList).hasSize(databaseSizeBeforeUpdate);
        PlanDePassation testPlanDePassation = planDePassationList.get(planDePassationList.size() - 1);
        assertThat(testPlanDePassation.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testPlanDePassation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlanDePassation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPlanDePassation.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanDePassation() throws Exception {
        int databaseSizeBeforeUpdate = planDePassationRepository.findAll().size();

        // Create the PlanDePassation
        PlanDePassationDTO planDePassationDTO = planDePassationMapper.toDto(planDePassation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanDePassationMockMvc.perform(put("/api/plan-de-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planDePassationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanDePassation in the database
        List<PlanDePassation> planDePassationList = planDePassationRepository.findAll();
        assertThat(planDePassationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanDePassation() throws Exception {
        // Initialize the database
        planDePassationRepository.saveAndFlush(planDePassation);

        int databaseSizeBeforeDelete = planDePassationRepository.findAll().size();

        // Delete the planDePassation
        restPlanDePassationMockMvc.perform(delete("/api/plan-de-passations/{id}", planDePassation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanDePassation> planDePassationList = planDePassationRepository.findAll();
        assertThat(planDePassationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
