package org.avenue1.design.web.rest;

import org.avenue1.design.DesignSvcApp;

import org.avenue1.design.domain.Container;
import org.avenue1.design.repository.ContainerRepository;
import org.avenue1.design.service.ContainerService;
import org.avenue1.design.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static org.avenue1.design.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.avenue1.design.domain.enumeration.UnitOfMeasureEnum;
/**
 * Test class for the ContainerResource REST controller.
 *
 * @see ContainerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DesignSvcApp.class)
public class ContainerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CONTAINER_ID = 1D;
    private static final Double UPDATED_CONTAINER_ID = 2D;

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_X_POS = 1D;
    private static final Double UPDATED_X_POS = 2D;

    private static final Double DEFAULT_Y_POS = 1D;
    private static final Double UPDATED_Y_POS = 2D;

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final UnitOfMeasureEnum DEFAULT_UNIT_OF_MEASURE = UnitOfMeasureEnum.INCH;
    private static final UnitOfMeasureEnum UPDATED_UNIT_OF_MEASURE = UnitOfMeasureEnum.MM;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restContainerMockMvc;

    private Container container;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContainerResource containerResource = new ContainerResource(containerService);
        this.restContainerMockMvc = MockMvcBuilders.standaloneSetup(containerResource)
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
    public static Container createEntity() {
        Container container = new Container()
            .name(DEFAULT_NAME)
            .containerID(DEFAULT_CONTAINER_ID)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .xPos(DEFAULT_X_POS)
            .yPos(DEFAULT_Y_POS)
            .created(DEFAULT_CREATED)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE)
            .data(DEFAULT_DATA)
            .modified(DEFAULT_MODIFIED);
        return container;
    }

    @Before
    public void initTest() {
        containerRepository.deleteAll();
        container = createEntity();
    }

    @Test
    public void createContainer() throws Exception {
        int databaseSizeBeforeCreate = containerRepository.findAll().size();

        // Create the Container
        restContainerMockMvc.perform(post("/api/containers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(container)))
            .andExpect(status().isCreated());

        // Validate the Container in the database
        List<Container> containerList = containerRepository.findAll();
        assertThat(containerList).hasSize(databaseSizeBeforeCreate + 1);
        Container testContainer = containerList.get(containerList.size() - 1);
        assertThat(testContainer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContainer.getContainerID()).isEqualTo(DEFAULT_CONTAINER_ID);
        assertThat(testContainer.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testContainer.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testContainer.getxPos()).isEqualTo(DEFAULT_X_POS);
        assertThat(testContainer.getyPos()).isEqualTo(DEFAULT_Y_POS);
        assertThat(testContainer.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testContainer.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testContainer.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testContainer.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    public void createContainerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = containerRepository.findAll().size();

        // Create the Container with an existing ID
        container.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restContainerMockMvc.perform(post("/api/containers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(container)))
            .andExpect(status().isBadRequest());

        // Validate the Container in the database
        List<Container> containerList = containerRepository.findAll();
        assertThat(containerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllContainers() throws Exception {
        // Initialize the database
        containerRepository.save(container);

        // Get all the containerList
        restContainerMockMvc.perform(get("/api/containers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(container.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].containerID").value(hasItem(DEFAULT_CONTAINER_ID.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].xPos").value(hasItem(DEFAULT_X_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].yPos").value(hasItem(DEFAULT_Y_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }
    
    @Test
    public void getContainer() throws Exception {
        // Initialize the database
        containerRepository.save(container);

        // Get the container
        restContainerMockMvc.perform(get("/api/containers/{id}", container.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(container.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.containerID").value(DEFAULT_CONTAINER_ID.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.xPos").value(DEFAULT_X_POS.doubleValue()))
            .andExpect(jsonPath("$.yPos").value(DEFAULT_Y_POS.doubleValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    public void getNonExistingContainer() throws Exception {
        // Get the container
        restContainerMockMvc.perform(get("/api/containers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateContainer() throws Exception {
        // Initialize the database
        containerService.save(container);

        int databaseSizeBeforeUpdate = containerRepository.findAll().size();

        // Update the container
        Container updatedContainer = containerRepository.findById(container.getId()).get();
        updatedContainer
            .name(UPDATED_NAME)
            .containerID(UPDATED_CONTAINER_ID)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .xPos(UPDATED_X_POS)
            .yPos(UPDATED_Y_POS)
            .created(UPDATED_CREATED)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .data(UPDATED_DATA)
            .modified(UPDATED_MODIFIED);

        restContainerMockMvc.perform(put("/api/containers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContainer)))
            .andExpect(status().isOk());

        // Validate the Container in the database
        List<Container> containerList = containerRepository.findAll();
        assertThat(containerList).hasSize(databaseSizeBeforeUpdate);
        Container testContainer = containerList.get(containerList.size() - 1);
        assertThat(testContainer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContainer.getContainerID()).isEqualTo(UPDATED_CONTAINER_ID);
        assertThat(testContainer.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testContainer.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testContainer.getxPos()).isEqualTo(UPDATED_X_POS);
        assertThat(testContainer.getyPos()).isEqualTo(UPDATED_Y_POS);
        assertThat(testContainer.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContainer.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testContainer.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testContainer.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    public void updateNonExistingContainer() throws Exception {
        int databaseSizeBeforeUpdate = containerRepository.findAll().size();

        // Create the Container

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContainerMockMvc.perform(put("/api/containers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(container)))
            .andExpect(status().isBadRequest());

        // Validate the Container in the database
        List<Container> containerList = containerRepository.findAll();
        assertThat(containerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteContainer() throws Exception {
        // Initialize the database
        containerService.save(container);

        int databaseSizeBeforeDelete = containerRepository.findAll().size();

        // Get the container
        restContainerMockMvc.perform(delete("/api/containers/{id}", container.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Container> containerList = containerRepository.findAll();
        assertThat(containerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Container.class);
        Container container1 = new Container();
        container1.setId("id1");
        Container container2 = new Container();
        container2.setId(container1.getId());
        assertThat(container1).isEqualTo(container2);
        container2.setId("id2");
        assertThat(container1).isNotEqualTo(container2);
        container1.setId(null);
        assertThat(container1).isNotEqualTo(container2);
    }
}
