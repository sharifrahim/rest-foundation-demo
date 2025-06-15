package com.github.sharifrahim.rest.rest.foundation.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.sharifrahim.rest.rest.foundation.demo.entity.ApiAuditTrail;
import com.github.sharifrahim.rest.rest.foundation.demo.repository.ApiAuditTrailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ApiAuditTrailService} that interacts with the database
 * via {@link ApiAuditTrailRepository} to perform CRUD operations on audit trail records.
 * 
 * Author: Sharif  
 * @see <a href="https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiAuditTrailServiceImpl implements ApiAuditTrailService {

    private final ApiAuditTrailRepository repository;

    /**
     * Saves a new audit trail record.
     *
     * @param auditTrail the audit trail to save
     * @return the saved entity
     */
    @Override
    public ApiAuditTrail save(ApiAuditTrail auditTrail) {
        log.debug("Saving new ApiAuditTrail with correlationId: {}", auditTrail.getCorrelationId());
        return repository.save(auditTrail);
    }

    /**
     * Finds an audit trail by its ID.
     *
     * @param id the record ID
     * @return an Optional containing the record if found
     */
    @Override
    public Optional<ApiAuditTrail> findById(Long id) {
        log.debug("Finding ApiAuditTrail by ID: {}", id);
        return repository.findById(id);
    }

    /**
     * Returns all audit trail records.
     *
     * @return list of all records
     */
    @Override
    public List<ApiAuditTrail> findAll() {
        log.debug("Retrieving all ApiAuditTrail records");
        return repository.findAll();
    }

    /**
     * Finds a record by its correlation ID.
     *
     * @param correlationId the correlation ID
     * @return an Optional containing the record if found
     */
    @Override
    public Optional<ApiAuditTrail> findByCorrelationId(String correlationId) {
        log.debug("Finding ApiAuditTrail by correlationId: {}", correlationId);
        return Optional.ofNullable(repository.findByCorrelationId(correlationId));
    }

    /**
     * Updates an existing audit trail.
     *
     * @param auditTrail the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if the ID is null or not found
     */
    @Override
    public ApiAuditTrail update(ApiAuditTrail auditTrail) {
        Long id = auditTrail.getId();
        if (id == null || !repository.existsById(id)) {
            log.warn("Update failed: invalid or missing ID");
            throw new IllegalArgumentException("Invalid ID or record not found.");
        }
        log.debug("Updating ApiAuditTrail with ID: {}", id);
        return repository.save(auditTrail);
    }

    /**
     * Deletes an audit trail by its ID.
     *
     * @param id the ID of the record to delete
     */
    @Override
    public void deleteById(Long id) {
        log.debug("Deleting ApiAuditTrail by ID: {}", id);
        repository.deleteById(id);
    }
}
