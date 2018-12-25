package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.SpringBootConfig;
import com.gmail.ivanjermakov1.messenger.auth.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDTO;
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
public class RegistrationTest {
	
	@Autowired
	private RegistrationController registrationController;
	
	@Test
	public void shouldRegisterUser() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password123"));
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldThrowRegistrationException_WithDuplication() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password123"));
	}
	
	@Test
	public void shouldNotThrowRegistrationException_WithDifferentLogin() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksk", "secure_password123"));
	}
	
	@Test
	public void shouldNotThrowRegistrationException_WithDifferentName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jackj", "Johnsond", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksk", "secure_password13"));
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldThrowRegistrationException_WithDifferentPassword() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password123"));
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "jacksj", "secure_password13"));
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldThrowRegistrationException_WithBlankFirstName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("", "Johnson", "jacksj", "secure_password123"));
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldThrowRegistrationException_WithBlankLastName() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "", "jacksj", "secure_password123"));
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldThrowRegistrationException_WithBlankLogin() throws RegistrationException {
		registrationController.register(
				new RegisterUserDTO("Jack", "Johnson", "", "secure_password123"));
	}
	
}
