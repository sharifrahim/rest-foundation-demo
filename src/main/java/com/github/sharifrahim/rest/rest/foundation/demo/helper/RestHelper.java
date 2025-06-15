package com.github.sharifrahim.rest.rest.foundation.demo.helper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sharifrahim.rest.rest.foundation.demo.entity.ApiAuditTrail;
import com.github.sharifrahim.rest.rest.foundation.demo.service.ApiAuditTrailService;
import com.github.sharifrahim.rest.rest.foundation.demo.strategy.RestRequestStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * Helper component to execute REST requests using a strategy pattern. Handles
 * logging, request/response processing, error handling, and audit trail
 * persistence.
 * 
 * Sensitive data in headers or payloads is sanitized before persisting.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@Component
@Slf4j
public class RestHelper {

	private final RestTemplate restTemplate;
	private final ApiAuditTrailService auditService;

	private static final Set<String> SENSITIVE_KEYS = Set.of("password", "token", "secret", "authorization", "apiKey");

	public RestHelper(@Qualifier("plainRestTemplate") RestTemplate restTemplate, ApiAuditTrailService auditService) {
		this.restTemplate = restTemplate;
		this.auditService = auditService;
	}

	/**
	 * Executes the given REST request strategy, logs metadata and saves audit.
	 *
	 * @param strategy the REST request strategy
	 * @param <REQ>    request type
	 * @param <RES>    response type
	 * @throws Exception if request or deserialization fails
	 */
	public <REQ, RES> void execute(RestRequestStrategy<REQ, RES> strategy) throws Exception {
		ZonedDateTime start = ZonedDateTime.now();
		String url = strategy.getUrl();
		HttpMethod method = strategy.getMethod();
		HttpEntity<REQ> requestEntity = strategy.buildRequestEntity();

		// Prepare audit log
		ApiAuditTrail audit = new ApiAuditTrail();
		audit.setMethod(method.name());
		audit.setUrl(url);
		audit.setRequestHeaders(toJson(requestEntity.getHeaders()));
		audit.setRequestBody(toJson(requestEntity.getBody()));
		audit.setTimestamp(start);

		try {
			// Inject body fields as query params for GET requests
			if (method == HttpMethod.GET) {
				url = appendQueryParamsFromBody(url, requestEntity);
				requestEntity = new HttpEntity<>(requestEntity.getHeaders()); // clear body
			}

			log.debug("Executing HTTP {} request to URL: {}", method, url);

			// Perform the REST call
			ResponseEntity<String> rawResponse = restTemplate.exchange(url, method, requestEntity, String.class);

			// Deserialize the response
			ObjectMapper mapper = new ObjectMapper();
			RES responseObj;
			try {
				String responseBody = rawResponse.getBody();
				if (responseBody.trim().startsWith("{") || responseBody.trim().startsWith("[")) {
					JsonNode jsonNode = mapper.readTree(responseBody);
					responseObj = mapper.treeToValue(jsonNode, strategy.getResponseType());
				} else {
					responseObj = strategy.getResponseType().cast(responseBody); // Plain string fallback
				}
			} catch (Exception ex) {
				audit.setStatus("FAILED");
				audit.setErrorMessage("Deserialization error: " + ex.getMessage());
				log.error("Failed to deserialize response", ex);
				throw new RuntimeException("Failed to map response to target type.");
			}

			audit.setStatus("SUCCESS");
			strategy.processResult(responseObj);
		} catch (Exception ex) {
			if (audit.getStatus() == null) {
				audit.setStatus("FAILED");
				audit.setErrorMessage(ex.getMessage());
			}
			log.error("REST request failed", ex);
			throw ex;
		} finally {
			int durationMs = (int) Duration.between(start, ZonedDateTime.now()).toMillis();
			audit.setDurationMs(durationMs);
			auditService.save(audit);
			log.info("API audit saved for method [{}] to URL [{}] in {} ms", method, url, durationMs);
		}
	}

	// Serialize and sanitize objects to JSON
	private String toJson(Object o) {
		try {
			Object sanitized = sanitizeObject(o);
			return new ObjectMapper().writeValueAsString(sanitized);
		} catch (Exception e) {
			log.warn("Failed to convert object to JSON", e);
			return "{}";
		}
	}

	// Mask sensitive data in headers or payloads
	private Object sanitizeObject(Object o) {
		if (o instanceof HttpHeaders) {
			HttpHeaders headers = (HttpHeaders) o;
			HttpHeaders sanitized = new HttpHeaders();
			headers.forEach((key, values) -> {
				if (isSensitiveKey(key)) {
					sanitized.put(key, Collections.singletonList("***"));
				} else {
					sanitized.put(key, values);
				}
			});
			return sanitized;
		} else if (o instanceof Map<?, ?>) {
			Map<String, Object> sanitized = new LinkedHashMap<>();
			Map<?, ?> map = (Map<?, ?>) o;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String keyStr = entry.getKey().toString();
				if (isSensitiveKey(keyStr)) {
					sanitized.put(keyStr, "***");
				} else {
					sanitized.put(keyStr, entry.getValue());
				}
			}
			return sanitized;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<String, Object> map = mapper.convertValue(o, new TypeReference<Map<String, Object>>() {
				});
				return sanitizeObject(map);
			} catch (IllegalArgumentException e) {
				return o;
			}
		}
	}

	// Checks if a key is sensitive
	private boolean isSensitiveKey(String key) {
		return SENSITIVE_KEYS.stream().anyMatch(s -> key.equalsIgnoreCase(s));
	}

	/**
	 * Converts request body fields into query parameters for GET requests.
	 *
	 * @param url    the base URL
	 * @param entity the request entity
	 * @return updated URL with query params
	 */
	private String appendQueryParamsFromBody(String url, HttpEntity<?> entity) {
		Object body = entity.getBody();
		if (body == null)
			return url;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(body, JsonNode.class);

		Map<String, String> flatParams = new LinkedHashMap<>();
		node.fields().forEachRemaining(entry -> {
			if (entry.getValue().isValueNode()) {
				flatParams.put(entry.getKey(), entry.getValue().asText());
			}
		});

		StringBuilder sb = new StringBuilder(url);
		if (!url.contains("?"))
			sb.append("?");
		else if (!url.endsWith("&"))
			sb.append("&");

		flatParams.forEach((k, v) -> {
			sb.append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append("=")
					.append(URLEncoder.encode(v, StandardCharsets.UTF_8)).append("&");
		});

		return sb.substring(0, sb.length() - 1); // Remove trailing '&'
	}
}
