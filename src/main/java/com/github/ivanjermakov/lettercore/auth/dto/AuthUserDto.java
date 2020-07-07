package com.github.ivanjermakov.lettercore.auth.dto;

public class AuthUserDto {

	public String login;
	public String password;

	public AuthUserDto(String login, String password) {
		this.login = login;
		this.password = password;
	}

}
