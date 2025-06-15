package com.github.sharifrahim.rest.rest.foundation.demo.factory;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.sharifrahim.rest.rest.foundation.demo.constant.Provider;
import com.github.sharifrahim.rest.rest.foundation.demo.manager.ProviderXTokenManager;
import com.github.sharifrahim.rest.rest.foundation.demo.manager.TokenManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory for managing TokenManager instances per provider. Initializes and
 * provides the correct TokenManager implementation based on the
 * {@link Provider} enum.
 * 
 * This enables a plug-and-play strategy for handling token logic for each
 * provider.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class TokenManagerFactory {

	// Map to store TokenManager implementations per provider
	private final Map<Provider, TokenManager> managers = new EnumMap<>(Provider.class);

	/**
	 * Initializes the TokenManager implementations after the bean is constructed.
	 */
	@PostConstruct
	private void init() {
		log.info("Initializing TokenManagerFactory...");

		// Register Provider X's token manager
		managers.put(Provider.PROVIDER_X, new ProviderXTokenManager());

		// TODO: Add additional providers here as needed
		log.info("TokenManagerFactory initialized with providers: {}", managers.keySet());
	}

	/**
	 * Returns the TokenManager implementation for the given provider.
	 *
	 * @param provider the provider enum
	 * @return the corresponding TokenManager, or null if not registered
	 */
	public TokenManager getManager(Provider provider) {
		log.debug("Fetching TokenManager for provider: {}", provider);
		return managers.get(provider);
	}
}
