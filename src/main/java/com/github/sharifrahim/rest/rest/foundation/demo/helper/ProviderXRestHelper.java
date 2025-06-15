package com.github.sharifrahim.rest.rest.foundation.demo.helper;

import org.springframework.stereotype.Component;

import com.github.sharifrahim.rest.rest.foundation.demo.constant.Provider;
import com.github.sharifrahim.rest.rest.foundation.demo.factory.TokenManagerFactory;
import com.github.sharifrahim.rest.rest.foundation.demo.strategy.ProviderXTokenDecoratedStrategy;
import com.github.sharifrahim.rest.rest.foundation.demo.strategy.RestRequestStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper class for executing REST requests specific to Provider X. This class
 * decorates the provided {@link RestRequestStrategy} with Provider X's token
 * and base URL before execution.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ProviderXRestHelper {

	private final RestHelper restHelper;
	private final TokenManagerFactory tokenManagerFactory;

	/**
	 * Executes a decorated REST request strategy for Provider X.
	 *
	 * @param strategy the original REST request strategy
	 * @param <REQ>    the request type
	 * @param <RES>    the response type
	 * @throws Exception if the underlying execution fails
	 */
	public <REQ, RES> void execute(RestRequestStrategy<REQ, RES> strategy) throws Exception {
		log.debug("Executing Provider X strategy with decoration.");

		// Decorate the strategy with Provider X token manager and base URL
		RestRequestStrategy<REQ, RES> decorated = new ProviderXTokenDecoratedStrategy<>(strategy,
				tokenManagerFactory.getManager(Provider.PROVIDER_X), "www.providerx.com");

		// Execute the decorated strategy using the shared RestHelper
		restHelper.execute(decorated);

		log.debug("Execution for Provider X completed.");
	}
}
