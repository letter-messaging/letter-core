package com.gmail.ivanjermakov1.messenger.exception;

public class InvalidPasswordException extends RegistrationException {
	
	public InvalidPasswordException() {
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}
	
}
