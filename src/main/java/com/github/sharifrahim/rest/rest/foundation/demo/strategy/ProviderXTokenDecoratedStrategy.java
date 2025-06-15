package com.github.sharifrahim.rest.rest.foundation.demo.strategy;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sharifrahim.rest.rest.foundation.demo.manager.TokenManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Decorator for {@link RestRequestStrategy} that adds a bearer token and base
 * URL for Provider X. This allows wrapping any REST request with
 * provider-specific token authentication and response handling.
 *
 * @param <REQ> the request DTO type
 * @param <RES> the response DTO type
 * 
 *              Author: Sharif
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@RequiredArgsConstructor
@Slf4j
public class ProviderXTokenDecoratedStrategy<REQ, RES> implements RestRequestStrategy<REQ, RES> {

	private final RestRequestStrategy<REQ, RES> delegate;
	private final TokenManager tokenManager;
	public final String baseUrl;

	/**
	 * Prepends the base URL to the original strategy's endpoint.
	 */
	@Override
	public String getUrl() throws Exception {
		String fullUrl = baseUrl + delegate.getUrl();
		log.debug("Constructed full URL for request: {}", fullUrl);
		return fullUrl;
	}

	/**
	 * Delegates HTTP method retrieval to the original strategy.
	 */
	@Override
	public HttpMethod getMethod() throws Exception {
		return delegate.getMethod();
	}

	/**
	 * Builds the request entity and injects the bearer token.
	 */
	@Override
	public HttpEntity<REQ> buildRequestEntity() throws Exception {
		HttpEntity<REQ> original = delegate.buildRequestEntity();

		// Clone original headers and add Authorization
		HttpHeaders headers = new HttpHeaders();
		HttpHeaders originalHeaders = original.getHeaders();
		if (originalHeaders != null) {
			headers.putAll(originalHeaders);
		}

		String token = tokenManager.getToken();
		headers.setBearerAuth(token);
		log.debug("Injected bearer token for Provider X");

		return new HttpEntity<>(original.getBody(), headers);
	}

	/**
	 * Delegates response type definition to the original strategy.
	 */
	@Override
	public Class<RES> getResponseType() throws Exception {
		return delegate.getResponseType();
	}

	/**
	 * Parses the response and throws an exception if the statusCode is not SUCCESS.
	 * Otherwise, delegates the result for further processing.
	 */
	@Override
	public void processResult(RES response) throws Exception {
		log.debug("Processing response with token-decorated strategy");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.valueToTree(response);

		String statusCode = findFirstMatchingKey(root, "statusCode");
		String statusMessage = findFirstMatchingKey(root, "statusMessage");

		if (!Arrays.asList("SUCCESS").contains(statusCode)) {
			log.error("API call failed with statusCode: {}, message: {}", statusCode, statusMessage);
			throw new RuntimeException(statusMessage != null ? statusMessage : "Unknown error from Provider X");
		}

		log.info("API call successful with statusCode: {}", statusCode);
		delegate.processResult(response);
	}

	/**
	 * Recursively searches a JSON node tree for the first matching key and returns
	 * its text value.
	 *
	 * @param node the root JSON node
	 * @param keys the key names to match
	 * @return the string value of the matching node, or null
	 */
	private String findFirstMatchingKey(JsonNode node, String... keys) {
		if (node == null || node.isNull())
			return null;

		if (node.isObject()) {
			for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
				Map.Entry<String, JsonNode> entry = it.next();

				for (String key : keys) {
					if (entry.getKey().equalsIgnoreCase(key) && entry.getValue().isValueNode()) {
						return entry.getValue().asText();
					}
				}

				String found = findFirstMatchingKey(entry.getValue(), keys);
				if (found != null)
					return found;
			}
		} else if (node.isArray()) {
			for (JsonNode child : node) {
				String found = findFirstMatchingKey(child, keys);
				if (found != null)
					return found;
			}
		}

		return null;
	}
}
