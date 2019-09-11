package com.gmail.ivanjermakov1.messenger.auth.controller;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
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

	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Register user.
	 *
	 * @param registerUserDto registration data
	 * @throws RegistrationException on invalid registration data. Not throws on successful registration
	 */
	@PostMapping
	public void register(@RequestBody RegisterUserDto registerUserDto) throws RegistrationException {
		userService.register(registerUserDto);
	}

}
