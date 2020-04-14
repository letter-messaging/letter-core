package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;

public interface RegistrationController {

	/**
	 * Register user.
	 *
	 * @param registerUserDto registration data
	 * @throws RegistrationException on invalid registration data. Not throws on successful registration
	 */
	void register(RegisterUserDto registerUserDto);

}
