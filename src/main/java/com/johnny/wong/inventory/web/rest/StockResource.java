package com.johnny.wong.inventory.web.rest;

import com.johnny.wong.inventory.domain.InternalTransferLog;
import com.johnny.wong.inventory.domain.Stock;
import com.johnny.wong.inventory.domain.User;
import com.johnny.wong.inventory.repository.InternalTransferLogRepository;
import com.johnny.wong.inventory.repository.StockRepository;
import com.johnny.wong.inventory.service.UserService;
import com.johnny.wong.inventory.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.johnny.wong.inventory.domain.Stock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockRepository stockRepository;
    private final InternalTransferLogRepository internalTransferLogRepository;
    private final UserService userService;

    public StockResource(StockRepository stockRepository, InternalTransferLogRepository internalTransferLogRepository, UserService userService) {
        this.stockRepository = stockRepository;
        this.internalTransferLogRepository = internalTransferLogRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /stocks} : Create a new stock.
     *
     * @param stock the stock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stock, or with status {@code 400 (Bad Request)} if the stock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stocks")
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stock result = stockRepository.save(stock);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stocks} : Updates an existing stock.
     *
     * @param stock the stock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stock,
     * or with status {@code 400 (Bad Request)} if the stock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stocks")
    public ResponseEntity<Stock> updateStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Stock result = stockRepository.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stocks} : get all the stocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stocks in body.
     */
    @GetMapping("/stocks")
    public List<Stock> getAllStocks() {
        log.debug("REST request to get all Stocks");
        return stockRepository.findAll();
    }

    /**
     * {@code GET  /stocks/:productCode} : get all the stocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stocks in body.
     */
    @GetMapping("/stocks/product/{productCode}")
    public List<Stock> getAllStocksByProductCode(@PathVariable String productCode) {
        log.debug("REST request to get all Stocks");
        return stockRepository.findAllByProduct_Code(productCode);
    }

    /**
     * {@code GET  /stocks/:id} : get the "id" stock.
     *
     * @param id the id of the stock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stocks/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<Stock> stock = stockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stock);
    }

    /**
     * {@code DELETE  /stocks/:id} : delete the "id" stock.
     *
     * @param id the id of the stock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code PUT  /stocks/transfer/internal} : Updates two existing stocks with internal transfer quantity.
     *
     * @param fromId,toId,quantity the stock ids used to update the stock by the quantity.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the message,
     * or with status {@code 400 (Bad Request)} if the given parameter(s) is/are not valid,
     * or with status {@code 500 (Internal Server Error)} if the stocks couldn't be updated.
     */
    @PutMapping("/stocks/transfer/internal")
    public ResponseEntity stockInternalTransfer(@RequestParam("from") Long fromId, @RequestParam("to") Long toId, @RequestParam("quantity") Long quantity) {
        Optional<Stock> optionalFrom = this.stockRepository.findById(fromId);
        Optional<Stock> optionalTo = this.stockRepository.findById(toId);

        if (optionalFrom.isPresent() && optionalTo.isPresent()) {
            Stock locationFrom = optionalFrom.get();
            Stock locationTo = optionalTo.get();

            if (quantity > 0 && locationFrom.getQuantity() >= quantity) {
                locationFrom.setQuantity(locationFrom.getQuantity() - quantity);
                locationTo.setQuantity(locationTo.getQuantity() + quantity);

                Optional<User> user = userService.getUserWithAuthorities();
                if (user.isPresent()) {
                    User existUser = user.get();
                    InternalTransferLog internalTransferLog = new InternalTransferLog();
                    internalTransferLog.setQuantity(quantity);
                    internalTransferLog.setUser(existUser);
                    internalTransferLog.setCreatedDate(ZonedDateTime.now());
                    internalTransferLog.setLocationFrom(locationFrom.getLocation());
                    internalTransferLog.setLocationTo(locationTo.getLocation());
                    internalTransferLog.setProductCode(locationFrom.getProduct().getCode());
                    InternalTransferLog ret = this.internalTransferLogRepository.save(internalTransferLog);
                    this.stockRepository.save(locationFrom);
                    this.stockRepository.save(locationTo);
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createAlert(applicationName, "Internal Transfer Success", "")).body(ret);
                }
                return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid Operation", "Your are not identify by the system!")).build();


            }
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid Operation", "stock quantity is not enough, Please refresh the page!")).build();
        }

        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid", "Stock data is not found by given parameters")).build();
    }

    /**
     * {@code PUT  /stocks/transfer/in} : Updates stock with given stock in quantity.
     *
     * @param stockId,inQuantity the stock ids used to update the stock.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the new Stock,
     * or with status {@code 400 (Bad Request)} if the given parameter(s) is/are not valid,
     * or with status {@code 500 (Internal Server Error)} if the stocks couldn't be updated.
     */
    @PutMapping("/stocks/transfer/in")
    public ResponseEntity stockInTransfer(@RequestParam("id") Long stockId, @RequestParam("quantity") Long inQuantity) {

        Optional<Stock> optionalStock = this.stockRepository.findById(stockId);

        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();

            if (inQuantity > 0) {
                stock.setQuantity(stock.getQuantity() + inQuantity);
                Stock retStock = this.stockRepository.save(stock);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert(applicationName, "Stock In Success", "")).body(retStock);
            }
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid Operation", "Increase quantity cannot be negative!")).build();
        }

        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid", "Stock data is not found by given parameters")).build();
    }

    /**
     * {@code PUT  /stocks/transfer/out} : Updates stock with given stock in quantity.
     *
     * @param stockId,outQuantity the stock ids used to update the stock.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the new Stock,
     * or with status {@code 400 (Bad Request)} if the given parameter(s) is/are not valid,
     * or with status {@code 500 (Internal Server Error)} if the stocks couldn't be updated.
     */
    @PutMapping("/stocks/transfer/out")
    public ResponseEntity stockOutTransfer(@RequestParam("id") Long stockId, @RequestParam("quantity") Long outQuantity) {

        Optional<Stock> optionalStock = this.stockRepository.findById(stockId);

        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();

            if (outQuantity > 0 && outQuantity <= stock.getQuantity()) {
                stock.setQuantity(stock.getQuantity() - outQuantity);
                Stock retStock = this.stockRepository.save(stock);

                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert(applicationName, "Stock Out Success", "")).body(retStock);
            }
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid Operation", "The existing stock is not Enough!/ Decrease quantity cannot be negative!")).build();
        }

        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(applicationName, false, "Internal Transfer", "Invalid", "Stock data is not found by given parameters")).build();
    }

}
