package com.johnny.wong.inventory.web.rest;

import com.johnny.wong.inventory.SimpleInventoryApp;
import com.johnny.wong.inventory.domain.InternalTransferLog;
import com.johnny.wong.inventory.repository.InternalTransferLogRepository;

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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.johnny.wong.inventory.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InternalTransferLogResource} REST controller.
 */
@SpringBootTest(classes = SimpleInventoryApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InternalTransferLogResourceIT {

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_FROM = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_TO = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_TO = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private InternalTransferLogRepository internalTransferLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternalTransferLogMockMvc;

    private InternalTransferLog internalTransferLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternalTransferLog createEntity(EntityManager em) {
        InternalTransferLog internalTransferLog = new InternalTransferLog()
            .productCode(DEFAULT_PRODUCT_CODE)
            .locationFrom(DEFAULT_LOCATION_FROM)
            .locationTo(DEFAULT_LOCATION_TO)
            .quantity(DEFAULT_QUANTITY)
            .createdDate(DEFAULT_CREATED_DATE);
        return internalTransferLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternalTransferLog createUpdatedEntity(EntityManager em) {
        InternalTransferLog internalTransferLog = new InternalTransferLog()
            .productCode(UPDATED_PRODUCT_CODE)
            .locationFrom(UPDATED_LOCATION_FROM)
            .locationTo(UPDATED_LOCATION_TO)
            .quantity(UPDATED_QUANTITY)
            .createdDate(UPDATED_CREATED_DATE);
        return internalTransferLog;
    }

    @BeforeEach
    public void initTest() {
        internalTransferLog = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllInternalTransferLogs() throws Exception {
        // Initialize the database
        internalTransferLogRepository.saveAndFlush(internalTransferLog);

        // Get all the internalTransferLogList
        restInternalTransferLogMockMvc.perform(get("/api/internal-transfer-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internalTransferLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].locationFrom").value(hasItem(DEFAULT_LOCATION_FROM)))
            .andExpect(jsonPath("$.[*].locationTo").value(hasItem(DEFAULT_LOCATION_TO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }
    
    @Test
    @Transactional
    public void getInternalTransferLog() throws Exception {
        // Initialize the database
        internalTransferLogRepository.saveAndFlush(internalTransferLog);

        // Get the internalTransferLog
        restInternalTransferLogMockMvc.perform(get("/api/internal-transfer-logs/{id}", internalTransferLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internalTransferLog.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.locationFrom").value(DEFAULT_LOCATION_FROM))
            .andExpect(jsonPath("$.locationTo").value(DEFAULT_LOCATION_TO))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }
    @Test
    @Transactional
    public void getNonExistingInternalTransferLog() throws Exception {
        // Get the internalTransferLog
        restInternalTransferLogMockMvc.perform(get("/api/internal-transfer-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
