package com.gmail.ivanjermakov1.messenger.test.integration;

import com.gmail.ivanjermakov1.messenger.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
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
public class RegistrationTest {

	@Autowired
	private RegistrationController registrationController;

	@Test
	public void shouldRegisterUser() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithDuplication() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
	}

	@Test
	public void shouldNotThrowException_WithDifferentLogin() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksk", "secure_password123"));
	}

	@Test
	public void shouldNotThrowException_WithDifferentName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jackj", "Johnsond", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksk", "secure_password13"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithDifferentPassword() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password13"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithBlankFirstName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("", "Johnson", "jacksj", "secure_password123"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithBlankLastName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "", "jacksj", "secure_password123"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithBlankLogin() throws RegistrationException {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "", "secure_password123"));
	}

}
