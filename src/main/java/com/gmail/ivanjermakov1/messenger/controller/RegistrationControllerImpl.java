package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import com.gmail.ivanjermakov1.messenger.validator.RegisterUserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
@Transactional
public class RegistrationControllerImpl implements RegistrationController {

	private final UserService userService;
	private final RegisterUserDtoValidator registerUserDtoValidator;

	@Autowired
	public RegistrationControllerImpl(UserService userService, RegisterUserDtoValidator registerUserDtoValidator) {
		this.userService = userService;
		this.registerUserDtoValidator = registerUserDtoValidator;
	}

	@Override
	@PostMapping
	public void register(@RequestBody RegisterUserDto registerUserDto) {
		registerUserDtoValidator.throwInvalid(registerUserDto);

		userService.register(registerUserDto);
	}

}
