package com.github.sharifrahim.rest.rest.foundation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sharifrahim.rest.rest.foundation.demo.entity.ApiAuditTrail;

/**
 * Repository interface for {@link ApiAuditTrail} entity. Extends
 * {@link JpaRepository} to provide CRUD operations and custom query methods.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@Repository
public interface ApiAuditTrailRepository extends JpaRepository<ApiAuditTrail, Long> {

	/**
	 * Finds an audit trail entry by its correlation ID.
	 *
	 * @param correlationId the correlation ID to search by
	 * @return the matching {@link ApiAuditTrail}, or null if not found
	 */
	ApiAuditTrail findByCorrelationId(String correlationId);
}
