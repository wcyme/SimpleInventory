package com.johnny.wong.inventory.web.rest;

import com.johnny.wong.inventory.domain.InternalTransferLog;
import com.johnny.wong.inventory.repository.InternalTransferLogRepository;
import com.johnny.wong.inventory.web.rest.errors.BadRequestAlertException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.johnny.wong.inventory.domain.InternalTransferLog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InternalTransferLogResource {

    private final Logger log = LoggerFactory.getLogger(InternalTransferLogResource.class);

    private final InternalTransferLogRepository internalTransferLogRepository;

    public InternalTransferLogResource(InternalTransferLogRepository internalTransferLogRepository) {
        this.internalTransferLogRepository = internalTransferLogRepository;
    }

    /**
     * {@code GET  /internal-transfer-logs} : get all the internalTransferLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internalTransferLogs in body.
     */
    @GetMapping("/internal-transfer-logs")
    public ResponseEntity<List<InternalTransferLog>> getAllInternalTransferLogs(Pageable pageable) {
        log.debug("REST request to get a page of InternalTransferLogs");
        Page<InternalTransferLog> page = internalTransferLogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /internal-transfer-logs/:id} : get the "id" internalTransferLog.
     *
     * @param id the id of the internalTransferLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internalTransferLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internal-transfer-logs/{id}")
    public ResponseEntity<InternalTransferLog> getInternalTransferLog(@PathVariable Long id) {
        log.debug("REST request to get InternalTransferLog : {}", id);
        Optional<InternalTransferLog> internalTransferLog = internalTransferLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(internalTransferLog);
    }
}
