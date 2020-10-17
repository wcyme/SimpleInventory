package com.johnny.wong.inventory.web.rest;

import com.johnny.wong.inventory.domain.Product;
import com.johnny.wong.inventory.domain.Stock;
import com.johnny.wong.inventory.repository.ProductRepository;
import com.johnny.wong.inventory.repository.StockRepository;
import com.johnny.wong.inventory.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.undertow.predicate.FalsePredicate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.johnny.wong.inventory.domain.Product} {@link com.johnny.wong.inventory.domain.Stock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ImportCsvFileResource {

    private final Logger log = LoggerFactory.getLogger(ImportCsvFileResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public ImportCsvFileResource(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /upload/products} : Import products by a csv file.
     *
     * @param uploadedFile the csv file contains products info.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if something is stored in the database, or with status {@code 400 (Bad Request)} if the file format is invalid.
     * @throws IOException if the file cannot be read normally.
     */
    @PostMapping("/upload/products")
    public ResponseEntity uploadProductCsvFile(@RequestParam("file")MultipartFile uploadedFile) throws IOException {
        log.debug("REST request to handle multipartFile : {}", uploadedFile.getName());

        if (uploadedFile.getOriginalFilename().endsWith(".csv")){
            BufferedReader filerReader = new BufferedReader(new InputStreamReader((uploadedFile.getInputStream()), "UTF-8"));
            CSVParser csvParser = new CSVParser(filerReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<Product> productList = new ArrayList<Product>();
            for (CSVRecord csvRecord: csvRecords){
                Product product = new Product();
                product.setCode(csvRecord.get("code"));
                if (this.productRepository.findOneByCode(product.getCode()).isPresent()){
                    continue;
                }
                product.setName(csvRecord.get("name"));
                product.setWeight(new BigDecimal(csvRecord.get("weight")));
                if (product.getCode().isEmpty() || product.getName().isEmpty() || product.getWeight() == BigDecimal.ZERO){
                    continue;
                }
                productList.add(product);
                log.debug("****Look****: code:{}, name:{}, weight:{}", product.getCode(), product.getName(), product.getWeight());
            }

            String retLength = Integer.toString(productList.size());
            if (productList.size() > 0){
                this.productRepository.saveAll(productList);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert(applicationName, retLength + " products are imported to server!", retLength)).build();
            }
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, retLength + " products are imported to server!", retLength)).build();

        }
        else{
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, false,"Import Product CSV File", "Invalid", "Invalid file format.")).build();
        }

    }

    /**
     * {@code POST  /upload/stocks} : Import products by a csv file.
     *
     * @param uploadedFile the csv file contains stock data info.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if something is stored in the database, or with status {@code 400 (Bad Request)} if the file format is invalid.
     * @throws IOException if the file cannot be read normally.
     */
    @PostMapping("/upload/stocks")
    public ResponseEntity uploadStockCsvFile(@RequestParam("file")MultipartFile uploadedFile) throws IOException {
        log.debug("REST request to handle multipartFile : {}", uploadedFile.getOriginalFilename());

        if (uploadedFile.getOriginalFilename().endsWith(".csv")){
            BufferedReader filerReader = new BufferedReader(new InputStreamReader((uploadedFile.getInputStream()), "UTF-8"));
            CSVParser csvParser = new CSVParser(filerReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            if (!(csvParser.getHeaderNames().contains("product_code") && csvParser.getHeaderNames().contains("location") && csvParser.getHeaderNames().contains("quantity"))){
                return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(applicationName, false,"Import Stock CSV File", "Invalid", "Invalid csv header format.")).build();
            }
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<Stock> stockList = new ArrayList<Stock>();
            for (CSVRecord csvRecord: csvRecords){
                Stock stock = new Stock();

                String productCode = csvRecord.get("product_code");
                Optional<Product> optionalProductProduct = this.productRepository.findOneByCode(productCode);
                if (optionalProductProduct.isPresent()){
                    Product product = optionalProductProduct.get();
                    stock.setProduct(product);
                }
                else{
                    continue;
                }
                stock.setLocation(csvRecord.get("location"));
                stock.setQuantity(new Long(csvRecord.get("quantity")));
                if (stock.getLocation().isEmpty() || stock.getQuantity() < 0){
                    continue;
                }
                stockList.add(stock);
                log.debug("****Look****: location:{}, quantity:{}, product_code:{}", stock.getLocation(), stock.getQuantity(), stock.getProduct().getCode());

            }

            String retLength = Integer.toString(stockList.size());
            if (stockList.size() > 0){
                this.stockRepository.saveAll(stockList);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert(applicationName, retLength + " stock data are imported to server!", retLength)).build();
            }
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, retLength + " stock data are imported to server!", retLength)).build();

        }
        else{
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, false,"Import Stock CSV File", "Invalid", "Invalid file format.")).build();
        }

    }
}
