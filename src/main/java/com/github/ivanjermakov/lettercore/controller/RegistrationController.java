package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.UserService;
import com.github.ivanjermakov.lettercore.validator.RegisterUserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
@Transactional
public class RegistrationController {

	private final UserService userService;
	private final RegisterUserDtoValidator registerUserDtoValidator;

	@Autowired
	public RegistrationController(UserService userService, RegisterUserDtoValidator registerUserDtoValidator) {
		this.userService = userService;
		this.registerUserDtoValidator = registerUserDtoValidator;
	}

	/**
	 * Register user.
	 *
	 * @param registerUserDto registration data
	 * @throws RegistrationException on invalid registration data. Not throws on successful registration
	 */
	@PostMapping
	public void register(@RequestBody RegisterUserDto registerUserDto) {
		registerUserDtoValidator.throwInvalid(registerUserDto);

		userService.register(registerUserDto);
	}

}
