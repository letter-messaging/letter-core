package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;

public interface RegistrationController {

	/**
	 * Register user.
	 *
	 * @param registerUserDto registration data
	 * @throws RegistrationException on invalid registration data. Not throws on successful registration
	 */
	void register(RegisterUserDto registerUserDto);

}
