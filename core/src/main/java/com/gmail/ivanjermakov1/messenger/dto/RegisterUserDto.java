package com.gmail.ivanjermakov1.messenger.dto;

import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;

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

	public void validate() throws RegistrationException, InvalidEntityException {
		if (firstName == null || lastName == null || login == null || password == null)
			throw new RegistrationException("fields cannot be blank.");

		firstName = firstName.trim();
		lastName = lastName.trim();
		login = login.trim();

		if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty()) {
			throw new RegistrationException("fields cannot be blank.");
		}

		validatePassword(password);
	}

	private void validatePassword(String password) throws InvalidEntityException {
		if (password.length() < 8 || password.length() > 32)
			throw new InvalidEntityException("password must be between 8 and 32 characters long.");
	}

}
