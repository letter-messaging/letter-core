package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.auth.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.exception.InvalidPasswordException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
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

	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowException_WithEmptyPassword() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", ""));
	}

	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowException_WithLessThen8Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "1234567"));
	}

	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowException_WithMoreThen32Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jackj", "1234567812345678123456781234567812345678"));
	}

}
