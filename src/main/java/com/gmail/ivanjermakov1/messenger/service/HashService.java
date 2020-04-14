package com.gmail.ivanjermakov1.messenger.service;

public interface HashService {

	/**
	 * Calculates hash value for specified input
	 *
	 * @param input input string
	 * @return hashed value
	 */
	String getHash(String input);

	/**
	 * Checks whether given plaintext password input string
	 * to a stored hash value.
	 *
	 * @param input  input string
	 * @param stored stored hash value
	 */
	boolean check(String input, String stored);

}