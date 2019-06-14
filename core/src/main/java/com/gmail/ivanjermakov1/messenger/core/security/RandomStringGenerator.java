package com.gmail.ivanjermakov1.messenger.core.security;

import org.apache.commons.text.CharacterPredicates;

public class RandomStringGenerator {
	
	private static org.apache.commons.text.RandomStringGenerator randomStringGenerator =
			new org.apache.commons.text.RandomStringGenerator.Builder()
					.withinRange('0', 'z')
					.filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
					.build();
	
	public static String generate(int length) {
		return randomStringGenerator.generate(length);
	}
	
}
