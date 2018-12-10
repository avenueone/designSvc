package org.avenue1.design.web.rest;

import org.avenue1.design.DesignSvcApp;

import org.avenue1.design.domain.Design;
import org.avenue1.design.repository.DesignRepository;
import org.avenue1.design.service.DesignService;
import org.avenue1.design.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static org.avenue1.design.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.avenue1.design.domain.enumeration.UnitOfMeasureEnum;
/**
 * Test class for the DesignResource REST controller.
 *
 * @see DesignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DesignSvcApp.class)
public class DesignResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_INSTRUMENT_TYPES = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUMENT_TYPES = "BBBBBBBBBB";

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final UnitOfMeasureEnum DEFAULT_UNIT_OF_MEASURE = UnitOfMeasureEnum.POINT;
    private static final UnitOfMeasureEnum UPDATED_UNIT_OF_MEASURE = UnitOfMeasureEnum.CM;

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BACKGROUND = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND = "BBBBBBBBBB";

    @Autowired
    private DesignRepository designRepository;

    @Mock
    private DesignRepository designRepositoryMock;

    @Mock
    private DesignService designServiceMock;

    @Autowired
    private DesignService designService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restDesignMockMvc;

    private Design design;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DesignResource designResource = new DesignResource(designService);
        this.restDesignMockMvc = MockMvcBuilders.standaloneSetup(designResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Design createEntity() {
        Design design = new Design()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .instrumentTypes(DEFAULT_INSTRUMENT_TYPES)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE)
            .created(DEFAULT_CREATED)
            .background(DEFAULT_BACKGROUND);
        return design;
    }

    @Before
    public void initTest() {
        designRepository.deleteAll();
        design = createEntity();
    }

    @Test
    public void createDesign() throws Exception {
        int databaseSizeBeforeCreate = designRepository.findAll().size();

        // Create the Design
        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isCreated());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeCreate + 1);
        Design testDesign = designList.get(designList.size() - 1);
        assertThat(testDesign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDesign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDesign.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testDesign.getInstrumentTypes()).isEqualTo(DEFAULT_INSTRUMENT_TYPES);
        assertThat(testDesign.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testDesign.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testDesign.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testDesign.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDesign.getBackground()).isEqualTo(DEFAULT_BACKGROUND);
    }

    @Test
    public void createDesignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = designRepository.findAll().size();

        // Create the Design with an existing ID
        design.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = designRepository.findAll().size();
        // set the field null
        design.setName(null);

        // Create the Design, which fails.

        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllDesigns() throws Exception {
        // Initialize the database
        designRepository.save(design);

        // Get all the designList
        restDesignMockMvc.perform(get("/api/designs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(design.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].instrumentTypes").value(hasItem(DEFAULT_INSTRUMENT_TYPES.toString())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].background").value(hasItem(DEFAULT_BACKGROUND.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDesignsWithEagerRelationshipsIsEnabled() throws Exception {
        DesignResource designResource = new DesignResource(designServiceMock);
        when(designServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDesignMockMvc = MockMvcBuilders.standaloneSetup(designResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDesignMockMvc.perform(get("/api/designs?eagerload=true"))
        .andExpect(status().isOk());

        verify(designServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDesignsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DesignResource designResource = new DesignResource(designServiceMock);
            when(designServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDesignMockMvc = MockMvcBuilders.standaloneSetup(designResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDesignMockMvc.perform(get("/api/designs?eagerload=true"))
        .andExpect(status().isOk());

            verify(designServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getDesign() throws Exception {
        // Initialize the database
        designRepository.save(design);

        // Get the design
        restDesignMockMvc.perform(get("/api/designs/{id}", design.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(design.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.instrumentTypes").value(DEFAULT_INSTRUMENT_TYPES.toString()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.background").value(DEFAULT_BACKGROUND.toString()));
    }

    @Test
    public void getNonExistingDesign() throws Exception {
        // Get the design
        restDesignMockMvc.perform(get("/api/designs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDesign() throws Exception {
        // Initialize the database
        designService.save(design);

        int databaseSizeBeforeUpdate = designRepository.findAll().size();

        // Update the design
        Design updatedDesign = designRepository.findById(design.getId()).get();
        updatedDesign
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .instrumentTypes(UPDATED_INSTRUMENT_TYPES)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .created(UPDATED_CREATED)
            .background(UPDATED_BACKGROUND);

        restDesignMockMvc.perform(put("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDesign)))
            .andExpect(status().isOk());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeUpdate);
        Design testDesign = designList.get(designList.size() - 1);
        assertThat(testDesign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDesign.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDesign.getInstrumentTypes()).isEqualTo(UPDATED_INSTRUMENT_TYPES);
        assertThat(testDesign.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testDesign.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testDesign.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testDesign.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDesign.getBackground()).isEqualTo(UPDATED_BACKGROUND);
    }

    @Test
    public void updateNonExistingDesign() throws Exception {
        int databaseSizeBeforeUpdate = designRepository.findAll().size();

        // Create the Design

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignMockMvc.perform(put("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDesign() throws Exception {
        // Initialize the database
        designService.save(design);

        int databaseSizeBeforeDelete = designRepository.findAll().size();

        // Get the design
        restDesignMockMvc.perform(delete("/api/designs/{id}", design.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Design.class);
        Design design1 = new Design();
        design1.setId("id1");
        Design design2 = new Design();
        design2.setId(design1.getId());
        assertThat(design1).isEqualTo(design2);
        design2.setId("id2");
        assertThat(design1).isNotEqualTo(design2);
        design1.setId(null);
        assertThat(design1).isNotEqualTo(design2);
    }
}
