package com.github.sharifrahim.rest.rest.foundation.demo.manager;

/**
 * Interface for managing and retrieving authentication tokens for external
 * providers. Implementations may include logic for caching, refreshing, or
 * fetching tokens.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
public interface TokenManager {

	/**
	 * Retrieves a valid token for the associated provider.
	 *
	 * @return the bearer token as a String
	 * @throws Exception if token acquisition fails
	 */
	String getToken() throws Exception;
}
