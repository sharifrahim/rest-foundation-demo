package com.github.sharifrahim.rest.rest.foundation.demo.strategy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;

import com.github.sharifrahim.rest.rest.foundation.demo.dto.ProviderXRestDto.CheckAccountReqDto;
import com.github.sharifrahim.rest.rest.foundation.demo.dto.ProviderXRestDto.CheckAccountRespDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link RestRequestStrategy} for checking account details
 * with Provider X. Sends a POST request to `/account/check` and validates the
 * response content.
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@Slf4j
public class ProviderXCheckAccount implements RestRequestStrategy<CheckAccountReqDto, CheckAccountRespDto> {

	/**
	 * Returns the endpoint URL to check account.
	 */
	@Override
	public String getUrl() throws Exception {
		return "/account/check";
	}

	/**
	 * Returns the HTTP method used for the request.
	 */
	@Override
	public HttpMethod getMethod() throws Exception {
		return HttpMethod.POST;
	}

	/**
	 * Builds the request entity with the account number payload.
	 */
	@Override
	public HttpEntity<CheckAccountReqDto> buildRequestEntity() throws Exception {
		CheckAccountReqDto req = new CheckAccountReqDto();
		// TODO: Set the account number before sending request
		log.debug("Building request entity for Provider X account check");
		return new HttpEntity<>(req);
	}

	/**
	 * Specifies the response DTO type expected from the server.
	 */
	@Override
	public Class<CheckAccountRespDto> getResponseType() throws Exception {
		return CheckAccountRespDto.class;
	}

	/**
	 * Processes the response returned by the server. Validates essential fields are
	 * present.
	 */
	@Override
	public void processResult(CheckAccountRespDto response) throws Exception {
		log.debug("Processing response from Provider X");

		if (ObjectUtils.isEmpty(response.getAccountHolderName())) {
			log.error("Account holder name is missing in the response");
			throw new RuntimeException("Account holder name is missing");
		} else if (ObjectUtils.isEmpty(response.getIsActive())) {
			log.error("Account status is missing in the response");
			throw new RuntimeException("Account status is missing");
		}

		log.info("Account check passed for account holder: {}", response.getAccountHolderName());
	}
}
