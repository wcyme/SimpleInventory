package com.johnny.wong.inventory.web.rest;

import com.johnny.wong.inventory.SimpleInventoryApp;
import com.johnny.wong.inventory.domain.Stock;
import com.johnny.wong.inventory.repository.StockRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StockResource} REST controller.
 */
@SpringBootTest(classes = SimpleInventoryApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StockResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 0L;
    private static final Long UPDATED_QUANTITY = 1L;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMockMvc;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .location(DEFAULT_LOCATION)
            .quantity(DEFAULT_QUANTITY);
        return stock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .location(UPDATED_LOCATION)
            .quantity(UPDATED_QUANTITY);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();
        // Create the Stock
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testStock.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setQuantity(null);

        // Create the Stock, which fails.


        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .location(UPDATED_LOCATION)
            .quantity(UPDATED_QUANTITY);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStock.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
