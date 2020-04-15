package com.github.ivanjermakov.lettercore.auth.integration;

import com.github.ivanjermakov.lettercore.auth.controller.AuthController;
import com.github.ivanjermakov.lettercore.auth.controller.RegistrationController;
import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.user.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.service.UserService;
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
public class AuthTest {

	@Autowired
	private AuthController authController;

	@Autowired
	private UserService userService;

	@Autowired
	private RegistrationController registrationController;

	@Test
	public void shouldAuthenticate() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		String token = authController.authenticate("jackj", "password");

		Assert.assertNotNull(token);

		UserDto user = authController.validate(
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

		authController.authenticate("jackj", "not_password");
	}

	@Test(expected = AuthenticationException.class)
	public void shouldLogout() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		String token = authController.authenticate("jackj", "password");

		Assert.assertNotNull(token);

		User user = userService.authenticate(token);

		UserDto userDto = authController.validate(user, token);

		Assert.assertNotNull(userDto);
		Assert.assertEquals("jackj", userDto.login);

		authController.logout(user);

		authController.validate(user, token);
	}

}
