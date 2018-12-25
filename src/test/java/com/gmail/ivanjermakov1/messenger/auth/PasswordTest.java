package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.SpringBootConfig;
import com.gmail.ivanjermakov1.messenger.auth.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDTO;
import com.gmail.ivanjermakov1.messenger.exception.InvalidPasswordException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringBootConfig.class,
		initializers = ConfigFileApplicationContextInitializer.class)
@Transactional
public class PasswordTest {
	
	@Autowired
	private RegistrationController registrationController;
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithEmptyPassword() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jackj", ""));
	}
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithLessThen8Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jackj", "1234567"));
	}
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithMoreThen32Characters() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jackj", "1234567812345678123456781234567812345678"));
	}
	
}
