package com.gmail.ivanjermakov1.messenger.dto;

public class TestingUser {

	public UserDto user;
	public String token;

	public TestingUser() {
	}

	public TestingUser(UserDto user, String token) {
		this.user = user;
		this.token = token;
	}

}
