package com.github.sharifrahim.rest.rest.foundation.demo.strategy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * Strategy interface for executing an external REST API call. This interface
 * defines all necessary steps to perform and handle a REST request and
 * response.
 *
 * @param <REQ> the request payload type
 * @param <RES> the response payload type
 * 
 *              Author: Sharif
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
public interface RestRequestStrategy<REQ, RES> {

	/**
	 * Returns the endpoint URL to be called (relative or full).
	 *
	 * @return the target URL as a String
	 * @throws Exception in case of dynamic URL resolution failure
	 */
	String getUrl() throws Exception;

	/**
	 * Returns the HTTP method (GET, POST, PUT, DELETE, etc.) for the request.
	 *
	 * @return the HTTP method
	 * @throws Exception in case of configuration or state errors
	 */
	HttpMethod getMethod() throws Exception;

	/**
	 * Builds the full HTTP entity containing headers and body for the request.
	 *
	 * @return the request entity
	 * @throws Exception if entity building fails
	 */
	HttpEntity<REQ> buildRequestEntity() throws Exception;

	/**
	 * Provides the expected response type for deserialization.
	 *
	 * @return the class type of the response
	 * @throws Exception in case of type resolution errors
	 */
	Class<RES> getResponseType() throws Exception;

	/**
	 * Processes the deserialized response. May include validation or business
	 * logic.
	 *
	 * @param response the deserialized response object
	 * @throws Exception if response validation or processing fails
	 */
	void processResult(RES response) throws Exception;
}
