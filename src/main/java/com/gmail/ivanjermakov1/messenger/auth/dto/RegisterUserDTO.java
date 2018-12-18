package com.gmail.ivanjermakov1.messenger.auth.dto;

import com.gmail.ivanjermakov1.messenger.exception.InvalidPasswordException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;

public class RegisterUserDTO {
	
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	
	public RegisterUserDTO() {
	}
	
	public RegisterUserDTO(String firstName, String lastName, String login, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void validate() throws RegistrationException {
		firstName = firstName.trim();
		lastName = lastName.trim();
		login = login.trim();
		
		if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty()) {
			throw new RegistrationException("fields cannot be blank.");
		}
		
		validatePassword(password);
	}
	
	private void validatePassword(String password) throws InvalidPasswordException {
		if (password.length() < 8 || password.length() > 32)
			throw new InvalidPasswordException("password must be between 8 and 32 characters long.");
	}
	
}
