package com.gmail.ivanjermakov1.messenger.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class HashServiceImpl implements HashService {

	@Override
	public String getHash(String input) {
		return DigestUtils.sha256Hex(input);
	}

	@Override
	public boolean check(String input, String stored) {
		return getHash(input).equals(stored);
	}

}
