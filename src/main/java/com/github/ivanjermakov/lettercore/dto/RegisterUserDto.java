package com.github.ivanjermakov.lettercore.dto;

public class RegisterUserDto {

	public String firstName;
	public String lastName;
	public String login;
	public String password;

	public RegisterUserDto() {
	}

	public RegisterUserDto(String firstName, String lastName, String login, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
	}

}
