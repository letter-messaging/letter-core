package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.RegistrationController;
import com.github.ivanjermakov.lettercore.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.google.common.base.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PasswordTest {

	@Autowired
	private RegistrationController registrationController;

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithEmptyPassword() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", ""));
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithLessThen8Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", Strings.repeat("1", 7)));
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithMoreThen32Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", Strings.repeat("1", 33)));
	}

}
