package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.RegistrationController;
import com.github.ivanjermakov.lettercore.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
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
	public void shouldThrowException_WithDuplication() {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
	}

	@Test(expected = RegistrationException.class)
	public void shouldThrowException_WithDifferentPassword() {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password13"));
	}

	@Test
	public void shouldNotThrowException_WithDifferentLogin() {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksk", "secure_password123"));
	}

	@Test
	public void shouldNotThrowException_WithDifferentName() {
		registrationController.register(
				new RegisterUserDto("Jackj", "Johnsond", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "jacksk", "secure_password13"));
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithBlankFirstName() {
		registrationController.register(
				new RegisterUserDto("", "Johnson", "jacksj", "secure_password123"));
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithBlankLastName() {
		registrationController.register(
				new RegisterUserDto("Jack", "", "jacksj", "secure_password123"));
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowException_WithBlankLogin() {
		registrationController.register(
				new RegisterUserDto("Jack", "Johnson", "", "secure_password123"));
	}

}
