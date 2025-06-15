package com.github.sharifrahim.rest.rest.foundation.demo.entity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity representing API audit trail records. This captures details of each
 * API call for tracking, debugging, and auditing purposes.
 * 
 * Fields include request/response metadata, timestamps, and system-generated
 * audit info.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@Entity
@Table(name = "api_audit_trail_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ApiAuditTrail {

	// Primary key for the audit trail entry
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Unique ID used for correlating logs across services
	@Column(name = "correlation_id")
	private String correlationId;

	// Timestamp of the API call
	@Column(name = "timestamp")
	private ZonedDateTime timestamp;

	// HTTP method (GET, POST, etc.)
	private String method;

	// Full URL of the API call
	@Column(columnDefinition = "TEXT")
	private String url;

	// Request headers as a serialized string
	@Column(name = "request_headers", columnDefinition = "TEXT")
	private String requestHeaders;

	// Request body content
	@Column(name = "request_body", columnDefinition = "TEXT")
	private String requestBody;

	// HTTP response status code
	@Column(name = "response_status")
	private Integer responseStatus;

	// Response body content
	@Column(name = "response_body", columnDefinition = "TEXT")
	private String responseBody;

	// Response headers as a serialized string
	@Column(name = "response_headers", columnDefinition = "TEXT")
	private String responseHeaders;

	// Duration of the API call in milliseconds
	@Column(name = "duration_ms")
	private Integer durationMs;

	// Status of the operation (e.g., SUCCESS, FAILURE)
	private String status;

	// Any error message encountered during the API call
	@Column(name = "error_message", columnDefinition = "TEXT")
	private String errorMessage;

	// Metadata: who created the entry
	@CreatedBy
	@Column(name = "created_by")
	protected String createdBy;

	// Metadata: when the entry was created
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	// Metadata: who last modified the entry
	@LastModifiedBy
	@Column(name = "updated_by", updatable = false)
	protected String updatedBy;

	// Metadata: when the entry was last modified
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime updatedAt;
}
