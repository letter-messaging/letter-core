package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.controller.AuthenticationController;
import com.gmail.ivanjermakov1.messenger.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
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
	private RegistrationController registrationController;

	@Test
	public void shouldAuthenticate() throws RegistrationException, AuthenticationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "password")
		);

		String token = authenticationController.authenticate("jackj", "password");

		Assert.assertNotNull(token);

		UserDto user = authenticationController.validate(token);

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

		UserDto user = authenticationController.validate(token);

		Assert.assertNotNull(user);
		Assert.assertEquals("jackj", user.login);

		authenticationController.logout(token);

		authenticationController.validate(token);
	}

}
