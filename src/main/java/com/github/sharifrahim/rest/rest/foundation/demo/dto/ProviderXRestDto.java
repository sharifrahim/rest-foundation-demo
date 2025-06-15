package com.github.sharifrahim.rest.rest.foundation.demo.dto;

import lombok.Data;

/**
 * DTO definitions for REST interactions with Provider X. Encapsulates request
 * and response data structures.
 * 
 * This static container class includes: - CheckAccountReqDto: Request DTO to
 * check account details - CheckAccountRespDto: Response DTO from account check
 * 
 * Author: Sharif
 * 
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
public class ProviderXRestDto {

	/**
	 * Request DTO to check account status for a given account number.
	 */
	@Data
	public static class CheckAccountReqDto {

		// The account number to be checked
		private String accountNo;
	}

	/**
	 * Response DTO for account status check.
	 */
	@Data
	public static class CheckAccountRespDto {

		// Indicates whether the account is active
		private Boolean isActive;

		// Name of the account holder
		private String accountHolderName;
	}
}
