package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.AuthenticationController;
import com.github.ivanjermakov.lettercore.controller.RegistrationController;
import com.github.ivanjermakov.lettercore.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AuthenticationTest {

	@Autowired
	private AuthenticationController authenticationController;

	@Autowired
	private UserService userService;

	@Autowired
	private RegistrationController registrationController;

	@Test
	public void shouldAuthenticate() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		String token = authenticationController.authenticate("jackj", "password");

		Assert.assertNotNull(token);

		UserDto user = authenticationController.validate(
				userService.authenticate(token),
				token
		);

		Assert.assertNotNull(user);
		Assert.assertEquals("jackj", user.login);
	}

	@Test(expected = AuthenticationException.class)
	public void shouldThrowException_WithWrongPassword() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		authenticationController.authenticate("jackj", "not_password");
	}

	@Test(expected = AuthenticationException.class)
	public void shouldLogout() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		String token = authenticationController.authenticate("jackj", "password");

		Assert.assertNotNull(token);

		User user = userService.authenticate(token);

		UserDto userDto = authenticationController.validate(user, token);

		Assert.assertNotNull(userDto);
		Assert.assertEquals("jackj", userDto.login);

		authenticationController.logout(user);

		authenticationController.validate(user, token);
	}

}
