package com.github.sharifrahim.rest.rest.foundation.demo.service;

import java.util.List;
import java.util.Optional;

import com.github.sharifrahim.rest.rest.foundation.demo.entity.ApiAuditTrail;

/**
 * Service interface for managing API audit trail records.
 * Provides standard CRUD operations and search by correlation ID.
 * 
 * Author: Sharif  
 * @see <a href="https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
public interface ApiAuditTrailService {

	/**
	 * Persists a new audit trail record.
	 *
	 * @param auditTrail the audit trail to save
	 * @return the saved audit trail entity
	 */
	ApiAuditTrail save(ApiAuditTrail auditTrail);

	/**
	 * Finds an audit trail record by its ID.
	 *
	 * @param id the ID of the record
	 * @return an Optional containing the result if found
	 */
	Optional<ApiAuditTrail> findById(Long id);

	/**
	 * Returns all audit trail records.
	 *
	 * @return a list of all audit trails
	 */
	List<ApiAuditTrail> findAll();

	/**
	 * Finds an audit trail by correlation ID.
	 *
	 * @param correlationId the correlation ID
	 * @return an Optional containing the result if found
	 */
	Optional<ApiAuditTrail> findByCorrelationId(String correlationId);

	/**
	 * Updates an existing audit trail record.
	 *
	 * @param auditTrail the audit trail with updated fields
	 * @return the updated audit trail
	 */
	ApiAuditTrail update(ApiAuditTrail auditTrail);

	/**
	 * Deletes an audit trail record by ID.
	 *
	 * @param id the ID of the record to delete
	 */
	void deleteById(Long id);
}

