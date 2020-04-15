package com.github.ivanjermakov.lettercore.util;

import java.util.UUID;

public class TokenGenerator {

	public static String generate() {
		return UUID.randomUUID().toString();
	}

}
